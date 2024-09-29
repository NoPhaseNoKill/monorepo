package com.nophasenokill.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.security.MessageDigest

@CacheableTask
abstract class ComputeClassDigestTask : DefaultTask() {

    @get:InputFiles
    @get:Classpath
    abstract val classFiles: ConfigurableFileCollection

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun compute() {
        val digests = classFiles.files.map { file ->
            val fqClassName = extractFullyQualifiedClassName(file)

            // Compute the digest of the class file
            val digest = MessageDigest.getInstance("SHA-256")
            val fileBytes = file.readBytes()
            val fileDigest = digest.digest(fileBytes)
            val fileHash = BigInteger(1, fileDigest).toString(16).padStart(64, '0')

            fqClassName to fileHash
        }.toMap()

        outputFile.get().asFile.writeText(digests.entries.joinToString("\n") { "${it.key}: ${it.value}" })
        println("Computed digests for class files: $digests")
    }

    private fun extractFullyQualifiedClassName(file: File): String {
        FileInputStream(file).use { inputStream ->
            val classReader = ClassReader(inputStream)
            var fqClassName = ""
            classReader.accept(object : ClassVisitor(Opcodes.ASM9) {
                override fun visit(
                    version: Int, access: Int, name: String, signature: String?, superName: String?, interfaces: Array<out String>?
                ) {
                    fqClassName = name.replace('/', '.')
                }
            }, 0)
            return fqClassName
        }
    }
}


