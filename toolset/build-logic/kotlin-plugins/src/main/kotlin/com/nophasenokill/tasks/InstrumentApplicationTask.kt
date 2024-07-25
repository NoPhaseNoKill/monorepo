package com.nophasenokill.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.*
import java.io.File

abstract class InstrumentApplicationTask: DefaultTask() {
    @get:Input
    abstract val appName: Property<String>

    @get:Input
    abstract val appClassResourceName: Property<String>

    @get:InputDirectory
    abstract val inputDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun perform() {

        val classFilePath = "com/nophasenokill/${appName.get()}.class"
        val fileDir = File("${inputDir.get()}/classes/kotlin/main/com/nophasenokill")

        if(!fileDir.exists()) {
            fileDir.mkdirs()
        }

        val url = fileDir.resolve("${appName.get()}.class").toURI().toURL()
        val classFileInputStream = url.openStream()
        val classReader = ClassReader(classFileInputStream, )
        val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES)
        val classVisitor = MethodLoggerClassVisitor(classWriter)

        classReader.accept(classVisitor, 0)

        val bytecode = classWriter.toByteArray()

        val outputFile = outputDir.get().asFile.resolve(classFilePath)
        outputFile.parentFile.mkdirs()
        outputFile.writeBytes(bytecode)

        // val bytes = Files.readAllBytes(outputFile.toPath())
        // // val decompiledCode = decompileClass(bytes) // Makes it readable so in future we can write a test for this output
        //
        //
        // // val outputJavaFile = outputDir.get().asFile.resolve("instrumented-java-output.java")
        //
        // val outputJavaFile = outputDir.get().asFile.resolve(classFilePath.replace(".class", ".java"))
        //
        // if(!outputJavaFile.exists()) {
        //     outputJavaFile.createNewFile()
        // }

        // outputJavaFile.writeText(decompiledCode)
    }
}


// private fun decompileClass(bytes: ByteArray): String {
//     val tempClassFile = File.createTempFile("tempClass", ".class")
//     Files.write(tempClassFile.toPath(), bytes)
//
//     val outputDir = File("build/instrumented-class")
//     outputDir.mkdirs()
//
//     val rootDir = File("")
//
//     // Means we can just run the test suite or the main method and have required classes on the classpath for both
//     val classpath = System.getProperty("java.class.path") + File.pathSeparator + "${rootDir.absolutePath}/build/libs/example-of-instrumentation-application-0.1.local-dev.jar"
//     val args = arrayOf(
//         tempClassFile.absolutePath,
//         "--outputdir", outputDir.absolutePath,
//         "--extraclasspath", classpath
//     )
//
//     Main.main(args)
//
//     val decompiledFile = outputDir.walkTopDown()
//         .filter { it.isFile && it.extension == "java" }
//         .firstOrNull() ?: throw IllegalArgumentException("Decompiled file not found")
//
//     return Files.readString(decompiledFile.toPath())
// }


class MethodLoggerClassVisitor(classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM9, classVisitor) {
    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        println("Visiting method: $name")
        return MethodLoggerMethodVisitor(mv, name)
    }
}

class MethodLoggerMethodVisitor(methodVisitor: MethodVisitor, private val methodName: String) :
    MethodVisitor(Opcodes.ASM9, methodVisitor) {

    override fun visitCode() {
        super.visitCode()
        println("Instrumenting method: $methodName")
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        mv.visitLdcInsn("Entering method: $methodName")
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false)
    }

    override fun visitInsn(opcode: Int) {
        if (opcode in Opcodes.IRETURN..Opcodes.RETURN || opcode == Opcodes.ATHROW) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
            mv.visitLdcInsn("Exiting method: $methodName")
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false)
        }
        super.visitInsn(opcode)
    }
}

