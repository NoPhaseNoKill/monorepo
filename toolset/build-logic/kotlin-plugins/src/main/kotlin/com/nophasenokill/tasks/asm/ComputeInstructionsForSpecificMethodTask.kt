package com.nophasenokill.tasks.asm

import com.nophasenokill.tasks.asm.IdentifyCallExample.Level
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.util.Textifier
import org.objectweb.asm.util.TraceMethodVisitor
import java.io.IOException
import java.lang.invoke.MethodHandles
import java.util.*

abstract class ComputeInstructionsForSpecificMethodTask: DefaultTask() {

    @get:Input
    abstract val classFileLocation: Property<String>

    @TaskAction
    fun compute() {
        val me = MethodHandles.lookup().lookupClass()
        val classPath = "com/nophasenokill/tasks/asm/IdentifyCallExample.class"
        val classStream = me.classLoader.getResourceAsStream(classPath)

        if (classStream == null) {
            throw IOException("Class not found: $classPath")
        }

        val classReader = ClassReader(classStream)
        val cn = ClassNode()

        classReader.accept(cn, ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
        var toAnalyze: MethodNode? = null
        for (mn in cn.methods) if (mn.name == "toAnalyze") {
            toAnalyze = mn
            break
        }

        val invocations: MutableList<IntArray> = ArrayList()
        val instructions = toAnalyze!!.instructions

        println("Class name: " + me.name)

        val sources = IdentifyCall.getInputs(me.name.replace('.', '/'), toAnalyze)

        var ix = 0
        val num = instructions.size()
        while (ix < num) {
            val instr = instructions[ix]
            if (instr.type != AbstractInsnNode.METHOD_INSN) {
                ix++
                continue
            }
            val s = sources.getAllInputsOf(instr).stream()
                .mapToInt { insnNode: AbstractInsnNode? ->
                    instructions.indexOf(
                        insnNode
                    )
                }.summaryStatistics()
            s.accept(ix)
            invocations.add(intArrayOf(s.min, s.max))
            ix++
        }

        printIt(invocations, instructions)
    }

    private fun printIt(invocations: List<IntArray>, instructions: InsnList) {
        val levels = toTree(invocations)
        val toText = Textifier()
        val tmv = TraceMethodVisitor(toText)
        var ix = 0
        val num = instructions.size()
        while (ix < num) {
            val instr = instructions[ix]
            var line = false
            level@ for (l in levels) {
                if (ix >= l!!.lo && ix <= l.hi) {
                    for (b in l.branches) {
                        if (ix < b[0] || ix > b[1]) continue
                        print(if (line) (if (b[0] == ix) if (b[1] == ix) "─[" else "┬─" else if (b[1] == ix) "┴─" else "┼─") else (if (b[0] == ix) if (b[1] == ix) " [" else "┌─" else if (b[1] == ix) "└─" else "│ "))
                        line = line or (b[0] == ix || b[1] == ix)
                        continue@level
                    }
                }
                print(if (line) "──" else "  ")
            }
            instr.accept(tmv)
            print(toText.text[0])
            toText.text.clear()
            ix++
        }
    }

    fun toTree(list: List<IntArray>): List<Level?> {
        if (list.isEmpty()) return emptyList()
        if (list.size == 1) return listOf(Level(list[0]))
        list.sortedWith(Comparator.comparingInt { b: IntArray ->
            b[1] - b[0]
        })
        val l = ArrayList<Level?>()
        insert@ for (b in list) {
            for (level in l) if (level!!.insert(b)) continue@insert
            l.add(Level(b))
        }
        if (l.size > 1) Collections.reverse(l)
        return l
    }
}
