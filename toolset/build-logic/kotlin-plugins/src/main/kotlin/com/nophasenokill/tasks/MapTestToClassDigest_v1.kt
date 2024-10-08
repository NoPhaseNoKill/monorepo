import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter
import java.io.File
import java.io.FileInputStream

@CacheableTask
abstract class MapTestToClassDigest_v1 : DefaultTask() {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val digestFile: RegularFileProperty

    @get:InputFiles
    @get:Classpath
    abstract val testClasses: ConfigurableFileCollection

    @get:OutputFile
    abstract val testToDigestMapFile: RegularFileProperty

    @TaskAction
    fun mapTestClassesToDigests() {

        // val classDigests = digestFile.get().asFile.readLines().associate {
        //     val (className, hash) = it.split(": ")
        //     val normalizedClassName = className.substringBeforeLast(".class").replace("/", ".")
        //     println("[GARDO] Found digest: $normalizedClassName -> $hash")
        //     normalizedClassName to hash
        // }

        val classDigests = digestFile.get().asFile.readLines().associate {
            val (className, hash) = it.split(": ")
            className to hash
        }

        val valuesToKeys = classDigests.entries.joinToString(prefix = "\n", separator = ",\n") {
            "Key: ${it.key}, Value: ${it.value}"
        }

        println("[GARDO] Class digests: $valuesToKeys")

        // Dynamically generate the test-to-production class mapping
        val testToProductionMapping = generateTestToProductionMapping()

        println("[GARDO] testToProductionMapping: $testToProductionMapping")

        val testToDigestMap = testClasses.files.associate { testClass ->
            val testName = testClass.nameWithoutExtension
            val relatedClasses = testToProductionMapping[testName] ?: emptyList()

            // Collect the hashes of the related production classes
            val digests = relatedClasses.mapNotNull {
                println("""
                    [GARDO]
                    It is: ${it}
                    classDigests is: ${classDigests.entries.map { " key: ${it.key}, value: ${it.value}" }.joinToString { "," }}
                """.trimIndent())
                classDigests[it]
            }

            testName to if (digests.isNotEmpty()) digests.joinToString(", ") else "null"
        }

        testToDigestMapFile.get().asFile.writeText(testToDigestMap.entries.joinToString("\n") { "${it.key}: ${it.value}" })
        println("Mapped test classes to class digests: $testToDigestMap")
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

    // Programmatically generate the test-to-production class mapping
    private fun generateTestToProductionMapping(): Map<String, List<String>> {

        return testClasses.files.associate { testClass ->
            val fileName = extractFullyQualifiedClassName(testClass)

            println("Test class: ${testClass.name}")
            println("fileName: ${fileName}")
            val relatedProductionClasses = findProductionClassesUsedInTest(testClass)
            fileName to relatedProductionClasses
        }
    }

    private fun isValidProductionClass(className: String): Boolean {
        // Filter out standard library, test classes, and other irrelevant classes
        return !(className.startsWith("java.") ||
                className.startsWith("javax.") ||
                className.startsWith("kotlin.") ||
                className.startsWith("kotlinx.") ||
                className.startsWith("org.junit.") ||
                className.contains("Test") ||  // Filter out internal test-related classes
                className.contains("$"))        // Filter out inner classes and anonymous classes
    }

    // Analyzes the test class file to find which production classes it references
    private fun findProductionClassesUsedInTest(testClass: File): List<String> {
        val referencedClasses = mutableListOf<String>()

        println("[GARDO] OHAIIIIIIII")
        // Use ASM to analyze bytecode and find referenced classes
        FileInputStream(testClass).use { inputStream ->
            val classReader = ClassReader(inputStream)
            classReader.accept(object : ClassVisitor(Opcodes.ASM9) {
                override fun visitMethod(
                    access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?
                ): MethodVisitor {
                    return object : MethodVisitor(Opcodes.ASM9) {
                        override fun visitMethodInsn(
                            opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean
                        ) {
                            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)

                            // Print the method name and owner (class)
                            println("Method called: $name in class: $owner")

                            if (owner != null) {
                                // Convert internal class name format (e.g., "com/example/MyClass") to standard class name (e.g., "com.example.MyClass")
                                val className = owner.replace("/", ".")
                                println("Class name: $className")

                                // Check if the class is a valid production class (not part of the JVM, framework, or test class)
                                if (isValidProductionClass(className)) {
                                    // Add to the list of referenced classes if it’s a valid production class
                                    referencedClasses.add(className)
                                }
                            }
                        }
                    }
                }
            }, 0)
        }

        return referencedClasses
    }
}

class DynamicLinkingTransformer(cv: ClassVisitor?) : ClassVisitor(Opcodes.ASM9, cv) {
    private lateinit var className: String

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        className = name ?: ""
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return DynamicLinkingMethodVisitor(Opcodes.ASM9, mv, access, name, descriptor, className)
    }

    class DynamicLinkingMethodVisitor(
        api: Int,
        mv: MethodVisitor?,
        access: Int,
        private val methodName: String?,
        descriptor: String?,
        private val className: String
    ) : AdviceAdapter(api, mv, access, methodName, descriptor) {

        override fun onMethodEnter() {
            // Log method entry
            mv?.visitLdcInsn("Entering method: $className.$methodName")
            mv?.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "DynamicLinkLogger",
                "log",
                "(Ljava/lang/String;)V",
                false
            )
        }

        override fun onMethodExit(opcode: Int) {
            // Log method exit
            mv?.visitLdcInsn("Exiting method: $className.$methodName")
            mv?.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "DynamicLinkLogger",
                "log",
                "(Ljava/lang/String;)V",
                false
            )
        }
    }
}
