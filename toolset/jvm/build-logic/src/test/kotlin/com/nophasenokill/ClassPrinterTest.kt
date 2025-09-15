package com.nophasenokill

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ClassPrinterTest {

    @Test
    fun `should parse the runnable java class`() {

        val actualOutput = captureTestOutput {
            val printer = ClassPrinter(Opcodes.ASM9)
            val reader = ClassReader("java.lang.Runnable")
            reader.accept(printer, 0)
        }

        val expectedOutput = """
            java/lang/Runnable extends java/lang/Object {
             run()V
            }
        """.trimIndent()

        Assertions.assertEquals(
            expectedOutput,
            actualOutput
        )
    }

    @Test
    fun `should generate a java class`() {

        val actualOutput = captureTestOutput {
            val cw = ClassWriter(0)
            cw.visit(
                Opcodes.ASM9, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                "pkg/Comparable", null, "java/lang/Object",
                arrayOf("pkg/Mesurable")
            )

            cw.visitField(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "LESS", "I",
                null, -1
            ).visitEnd()

            cw.visitField(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "EQUAL", "I",
                null, 0
            ).visitEnd()

            cw.visitField(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "GREATER", "I",
                null, 1
            ).visitEnd()

            cw.visitMethod(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "compareTo",
                "(Ljava/lang/Object;)I", null, null
            ).visitEnd()

            cw.visitEnd()

            val bytes: ByteArray = cw.toByteArray()

            val printer = ClassPrinter(Opcodes.ASM9)
            val reader = ClassReader(bytes)
            reader.accept(printer, 0)
        }

        /*
            Represents the equivalent of:

            package pkg;

            public interface Comparable extends Mesurable {
                int LESS = -1;
                int EQUAL = 0;
                int GREATER = 1;
                int compareTo(Object o);
            }

         */

        val expectedOutput = """
            pkg/Comparable extends java/lang/Object {
             I LESS -1
             I EQUAL 0
             I GREATER 1
             compareTo(Ljava/lang/Object;)I
            }
        """.trimIndent()

        Assertions.assertEquals(
            expectedOutput,
            actualOutput
        )
    }

    fun captureTestOutput(block: () -> Unit): String {
        ByteArrayOutputStream().use { stream ->
            PrintStream(stream).use { printStream ->
                System.setOut(printStream)
                try {
                    block()
                } finally {
                    System.setOut(System.out)
                }
                return stream.toString().trim()
            }
        }
    }
}
