package com.nophasenokill


import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.asm.AsmVisitorWrapper
import net.bytebuddy.description.NamedElement
import net.bytebuddy.description.field.FieldDescription
import net.bytebuddy.description.field.FieldList
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.description.method.MethodList
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.implementation.Implementation
import net.bytebuddy.jar.asm.*
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.matcher.ElementMatchers.nameStartsWith
import net.bytebuddy.pool.TypePool
import net.bytebuddy.utility.JavaModule
import java.io.File
import java.lang.instrument.Instrumentation

object TestExecutionTrackerAgent {

    @JvmStatic
    fun premain(agentArgs: String?, instrumentation: Instrumentation) {
        println("[TRACKER] Test Execution Tracker Agent loaded")
        println("Agent args: $agentArgs")

        val testsToRunProperty = System.getProperty("test.classpath")

        if (testsToRunProperty != null) {
            println("[TRACKER] test.classpath property: $testsToRunProperty")

            val testsToRunPaths = testsToRunProperty.split(File.pathSeparatorChar)

            // Convert file paths to fully qualified class names
            val testsToRun = testsToRunPaths
                .filter { it.endsWith(".class") }
                .map {
                    it.substringAfter("test/")
                        .replace('/', '.')
                        .removeSuffix(".class")
                }

            val testPrefixes = testsToRun.map { it.substringBeforeLast('.') }

            println("[TRACKER] Tests to run (class names): $testsToRun")

            // Byte Buddy AgentBuilder setup
            AgentBuilder.Default()
                .type(testPrefixes.map { nameStartsWith<NamedElement>(it) }.reduce { acc, matcher -> acc.or(matcher) })
                .transform { builder: DynamicType.Builder<*>, typeDescription: TypeDescription, classLoader: ClassLoader?, module: JavaModule?, protectionDomain ->
                    println("[TRACKER] Transforming class: ${typeDescription.name}")
                    builder
                        .visit(AsmVisitorWrapper.ForDeclaredMethods()
                            .method(ElementMatchers.any(), MyMethodVisitorWrapper())
                        )
                        .visit(MyAsmClassVisitorWrapper())
                }
                .installOn(instrumentation)
        } else {
            println("[TRACKER] No test.classpath property found.")
        }
    }
}

class MyMethodVisitorWrapper : AsmVisitorWrapper.ForDeclaredMethods.MethodVisitorWrapper {
    override fun wrap(
        instrumentedType: TypeDescription,
        instrumentedMethod: MethodDescription,
        methodVisitor: MethodVisitor,
        implementationContext: Implementation.Context,
        typePool: TypePool,
        writerFlags: Int,
        readerFlags: Int,
    ): MethodVisitor {
        return MyMethodVisitor(Opcodes.ASM9, methodVisitor, instrumentedMethod.name)
    }
}

class MyAsmClassVisitorWrapper : AsmVisitorWrapper {
    override fun mergeWriter(flags: Int): Int {
        return flags or ClassWriter.COMPUTE_FRAMES
    }

    override fun mergeReader(flags: Int): Int {
        return flags or ClassReader.EXPAND_FRAMES
    }

    override fun wrap(
        instrumentedType: TypeDescription,
        classVisitor: ClassVisitor,
        implementationContext: Implementation.Context,
        typePool: TypePool,
        fields: FieldList<FieldDescription.InDefinedShape>,
        methods: MethodList<*>,
        writerFlags: Int,
        readerFlags: Int,
    ): ClassVisitor {
        return MyClassVisitor(Opcodes.ASM9, classVisitor)
    }
}

class MyClassVisitor(api: Int, classVisitor: ClassVisitor) : ClassVisitor(api, classVisitor) {
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?,
    ) {
        println("[TRACKER][CLASS] Class Name: ${name?.replace("/", ".")}, Super Class: ${superName?.replace("/", ".")}")
        interfaces?.forEach { println("[TRACKER][CLASS] Implements Interface: ${it.replace("/", ".")}") }
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?,
    ): MethodVisitor {
        println("[TRACKER][METHOD] Test Method: $name, Descriptor: $descriptor")

        val originalMethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        return MyMethodVisitor(Opcodes.ASM9, originalMethodVisitor, name)
    }
}

class MyMethodVisitor(api: Int, methodVisitor: MethodVisitor, private val methodName: String?) : MethodVisitor(api, methodVisitor) {
    override fun visitCode() {
        println("[TRACKER][CODE] Entering method body of $methodName")
        super.visitCode()
    }

    override fun visitMethodInsn(
        opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean,
    ) {
        val ownerClass = owner?.replace("/", ".")
        println("[TRACKER][INVOKE] Method Call: $name() in Class: $ownerClass with Descriptor: $descriptor")
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
    }

    override fun visitVarInsn(opcode: Int, variable: Int) {
        println("[TRACKER][VARIABLE] Accessing Local Variable at index: $variable with Opcode: $opcode")
        super.visitVarInsn(opcode, variable)
    }

    override fun visitEnd() {
        println("[TRACKER][CODE] Exiting method body of $methodName")
        super.visitEnd()
    }
}

