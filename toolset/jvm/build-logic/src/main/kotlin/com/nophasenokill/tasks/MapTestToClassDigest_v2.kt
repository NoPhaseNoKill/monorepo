package com.nophasenokill.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.io.File
import java.io.FileInputStream

@CacheableTask
abstract class MapTestToClassDigest_v2 : DefaultTask() {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val digestFile: RegularFileProperty

    @get:InputFiles
    @get:Classpath
    abstract val testClasses: ConfigurableFileCollection

    @get:OutputFile
    abstract val testToDigestMapFile: RegularFileProperty

    @get:Input
    abstract val previousTestDigestFileLocation: Property<String>

    @get:OutputFile
    abstract val testsNeedRerunFile: RegularFileProperty

    @get:OutputFile
    abstract val testsNeedRerunReasonFile: RegularFileProperty

    @TaskAction
    fun mapTestClassesToDigests() {
        println("Starting mapTestToClassDigests task")

        val previousDigestFile = File(previousTestDigestFileLocation.get())
        if (!previousDigestFile.exists()) {
            previousDigestFile.createNewFile()
            println("No previous digest file found, created empty file at: ${previousDigestFile.absolutePath}")
        }

        val previousTestDigestMap = previousDigestFile.readLines().associate {
            val (className, hash) = it.split(":")
            className to hash
        }

        // Step 2: Read the current digest file
        val classDigests = digestFile.get().asFile.readLines().associate {
            val (className, hash) = it.split(":")
            val normalizedClassName = className.replace("/", ".").substringBeforeLast(".class")
            normalizedClassName to hash
        }

        val testToProductionMapping = generateTestToProductionMapping()
        val rerunTests = mutableListOf<String>()
        val reasonsForRerun = mutableListOf<String>()

        testClasses.files.forEach { testClass ->
            val testName = testClass.nameWithoutExtension
            val relatedClasses = testToProductionMapping[testName] ?: emptyList()

            val digests = relatedClasses.mapNotNull { classDigests[it] }
            val currentDigest = if (digests.isNotEmpty()) digests.joinToString(", ") else "null"
            val previousDigest = previousTestDigestMap[testName]

            if (previousDigest == null || previousDigest != currentDigest) {
                rerunTests.add(testName)
                reasonsForRerun.add("Test $testName needs re-running due to changes in: $relatedClasses")
            }
        }

        testsNeedRerunFile.get().asFile.writeText(if (rerunTests.isNotEmpty()) "true" else "false")
        testsNeedRerunReasonFile.get().asFile.writeText(reasonsForRerun.joinToString("\n"))

        previousDigestFile.writeText(testToProductionMapping.entries.joinToString("\n") { "${it.key}: ${it.value}" })
    }

    private fun generateTestToProductionMapping(): Map<String, List<String>> {
        return testClasses.files.associate { testClass ->
            val testName = testClass.nameWithoutExtension
            val relatedProductionClasses = findProductionClassesUsedInTest(testClass)
            testName to relatedProductionClasses
        }
    }

    private fun findProductionClassesUsedInTest(testClass: File): List<String> {
        val referencedClasses = mutableListOf<String>()
        FileInputStream(testClass).use { inputStream ->
            val classReader = ClassReader(inputStream)
            classReader.accept(object : ClassVisitor(Opcodes.ASM9) {
                override fun visitMethod(
                    access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?
                ): MethodVisitor {
                    return object : MethodVisitor(Opcodes.ASM9) {
                        override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean) {
                            if (owner != null) {
                                referencedClasses.add(owner.replace("/", "."))
                            }
                        }
                    }
                }
            }, 0)
        }
        return referencedClasses.distinct()
    }
}
