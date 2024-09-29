package com.example

import org.objectweb.asm.*
import java.io.File
import java.io.FileWriter
import java.lang.instrument.ClassFileTransformer
import java.lang.instrument.Instrumentation
import java.security.ProtectionDomain

object Agent {
    @JvmStatic
    fun premain(agentArgs: String?, instrumentation: Instrumentation) {
        // Reset the log file at the start of the test suite
        MethodTracker.resetLog()
        instrumentation.addTransformer(MethodTrackingTransformer())
    }
}

object MethodTracker {
    private val logFile = File("test-method-mapping.txt")

    fun logTestStart(testClass: String, testMethod: String?) {
        logFile.appendText("Test Start: $testClass#$testMethod\n")
    }

    fun logTestEnd(testClass: String, testMethod: String?) {
        logFile.appendText("Test End: $testClass#$testMethod\n")
    }

    fun logMethod(method: String) {
        logFile.appendText("  Method: $method\n")
    }


    fun resetLog() {
        logFile.writeText("") // Clear the log before running tests
    }
}


class MethodTrackingClassVisitor(cv: ClassVisitor, private val className: String) : ClassVisitor(Opcodes.ASM9, cv) {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = cv.visitMethod(access, name, descriptor, signature, exceptions)
        var isTestMethod = false

        // Check if the method has the @Test annotation
        val methodVisitor = object : MethodVisitor(Opcodes.ASM9, mv) {
            override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
                if (descriptor == "Lorg/junit/Test;") {
                    isTestMethod = true
                }
                return super.visitAnnotation(descriptor, visible)
            }
        }

        return MethodTrackingMethodVisitor(Opcodes.ASM9, methodVisitor, className, name, isTestMethod)
    }
}


class MethodTrackingMethodVisitor(
    api: Int, mv: MethodVisitor,
    private val className: String,
    private val methodName: String?,
    private val isTestMethod: Boolean // Add this flag to determine if this is a test method
) : MethodVisitor(api, mv) {

    override fun visitCode() {
        if (isTestMethod) {
            // Log the start of the test method
            MethodTracker.logTestStart(className, methodName)
        }

        super.visitCode()
    }

    override fun visitInsn(opcode: Int) {
        super.visitInsn(opcode)

        if (isTestMethod && (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
            // Log the end of the test method
            MethodTracker.logTestEnd(className, methodName)
        }
    }
}


class MethodTrackingTransformer : ClassFileTransformer {
    override fun transform(
        loader: ClassLoader?,
        className: String?,
        classBeingRedefined: Class<*>?,
        protectionDomain: ProtectionDomain?,
        classfileBuffer: ByteArray?
    ): ByteArray? {
        if (className != null) {
            println("Instrumenting class: $className")

            val reader = ClassReader(classfileBuffer)
            val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)
            val visitor = MethodTrackingClassVisitor(writer, className)

            reader.accept(visitor, 0)

            return writer.toByteArray()
        }
        return null
    }
}

