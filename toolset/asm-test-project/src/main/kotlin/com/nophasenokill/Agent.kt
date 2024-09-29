package com.nophasenokill

import org.objectweb.asm.*
import java.lang.instrument.ClassFileTransformer
import java.lang.instrument.Instrumentation
import java.security.ProtectionDomain


object Agent : Opcodes {
    @kotlin.Throws(java.lang.Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
    }

    @JvmStatic
    fun premain(agentArgs: String?, instrumentation: Instrumentation) {
        println("Agent loaded")
        instrumentation.addTransformer(MethodTrackingTransformer(), false)
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
        if (className != null && className.startsWith("com/nophasenokill")) {
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

class MethodTrackingClassVisitor(cv: ClassVisitor, private val className: String) :
    ClassVisitor(Opcodes.ASM9, cv) {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return MethodTrackingMethodVisitor(Opcodes.ASM9, mv, className, name ?: "")
    }
}

class MethodTrackingMethodVisitor(
    api: Int,
    mv: MethodVisitor,
    private val className: String,
    private val methodName: String
) : MethodVisitor(api, mv) {

    override fun visitCode() {
        super.visitCode()
        // Ensure we log the start of each method being visited
        if (methodName != "<init>") {  // Exclude constructors
            val fullMethodName = "$className#$methodName"
            MethodTracker.logMethod(fullMethodName)
        }
    }

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        if (owner != null && name != null && name != "<init>") {
            val fullMethodName = "$owner#$name"
            MethodTracker.logMethod(fullMethodName)  // Log method invocations
        }
    }
}
