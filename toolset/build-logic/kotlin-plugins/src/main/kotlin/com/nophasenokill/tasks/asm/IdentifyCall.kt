package com.nophasenokill.tasks.asm

import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.AnalyzerException
import org.objectweb.asm.tree.analysis.SourceInterpreter
import org.objectweb.asm.tree.analysis.SourceValue
import java.util.*

/*
    https://stackoverflow.com/questions/60969392/java-asm-bytecode-find-all-instructions-belonging-to-a-specific-method-call
 */

class IdentifyCall private constructor(
    private val instructions: InsnList,
    private val sources: Map<AbstractInsnNode, MutableSet<SourceValue>>,
    private val conditionals: TreeMap<IntArray, AbstractInsnNode>,
) {
    init {
        println("IdentifyCall created with " + instructions.size() + " instructions.")
    }

    fun getAllInputsOf(instr: AbstractInsnNode): Set<AbstractInsnNode> {
        println("Getting all inputs of instruction: $instr")

        if (!sources.containsKey(instr)) {
            println("No source found for instruction: $instr")
            return emptySet() // Add safety in case source is null
        }

        val source: MutableSet<AbstractInsnNode> = HashSet()
        val pending: MutableList<SourceValue> = ArrayList(
            sources[instr]
        )
        println("Initial pending size: " + pending.size)

        for (pIx in pending.indices) {
            val sv = pending[pIx]
            val branch = sv.insns.size > 1
            println("Analyzing SourceValue with branch size: " + sv.insns.size)

            for (`in` in sv.insns) {
                try {
                    println("Checking instruction: $`in`")
                    if (source.add(`in`)) {
                        println("Added instruction to source: $`in`")
                        pending.addAll(sources.getOrDefault(`in`, emptySet()))
                        println("Pending size after adding: " + pending.size)
                    }

                    if (branch) {
                        val ix = instructions.indexOf(`in`)
                        conditionals.forEach { (b: IntArray, i: AbstractInsnNode) ->
                            if (b[0] <= ix && b[1] >= ix && source.add(i)) {
                                println("Adding conditional instruction: $i")
                                pending.addAll(
                                    sources.getOrDefault(
                                        i,
                                        emptySet()
                                    )
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    System.err.println("Error while processing instruction: " + `in` + ". Error: " + e.message)
                    e.printStackTrace()
                }
            }
        }
        println("Total source size: " + source.size)
        return source
    }

    companion object {
        @Throws(AnalyzerException::class)
        fun getInputs(internalClassName: String, toAnalyze: MethodNode): IdentifyCall {
            println("Analyzing method: " + toAnalyze.name + " in class: " + internalClassName)
            val instructions = toAnalyze.instructions
            println("Method contains " + instructions.size() + " instructions.")

            val sources: MutableMap<AbstractInsnNode, MutableSet<SourceValue>> = HashMap()

            val i: SourceInterpreter = object : SourceInterpreter(
                ASM9
            ) {
                override fun unaryOperation(insn: AbstractInsnNode, value: SourceValue): SourceValue {
                    println("Unary operation on instruction: $insn")
                    sources.computeIfAbsent(
                        insn
                    ) { x: AbstractInsnNode? -> HashSet() }
                        .add(value)
                    return super.unaryOperation(insn, value)
                }

                override fun binaryOperation(insn: AbstractInsnNode, v1: SourceValue, v2: SourceValue): SourceValue {
                    println("Binary operation on instruction: $insn")
                    addAll(insn, Arrays.asList(v1, v2))
                    return super.binaryOperation(insn, v1, v2)
                }

                override fun ternaryOperation(
                    insn: AbstractInsnNode,
                    v1: SourceValue,
                    v2: SourceValue,
                    v3: SourceValue,
                ): SourceValue {
                    println("Ternary operation on instruction: $insn")
                    addAll(insn, Arrays.asList(v1, v2, v3))
                    return super.ternaryOperation(insn, v1, v2, v3)
                }

                override fun naryOperation(insn: AbstractInsnNode, values: List<SourceValue>): SourceValue {
                    println("N-ary operation on instruction: $insn")
                    addAll(insn, values)
                    return super.naryOperation(insn, values)
                }

                private fun addAll(insn: AbstractInsnNode, values: List<SourceValue>) {
                    println("Adding all values for instruction: $insn")
                    sources.computeIfAbsent(
                        insn
                    ) { x: AbstractInsnNode? -> HashSet() }
                        .addAll(values)
                }
            }

            val conditionals = TreeMap<IntArray, AbstractInsnNode>(Comparator.comparingInt { a: IntArray ->
                a[0]
            }.thenComparingInt { a: IntArray -> a[1] })


            val analyzer: Analyzer<SourceValue?> = object : Analyzer<SourceValue?>(i) {
                override fun newControlFlowEdge(insn: Int, successor: Int) {
                    try {
                        println("New control flow edge from $insn to $successor")
                        if (insn != successor - 1) {
                            val instruction = instructions[insn]
                            val dep: Set<SourceValue>? = sources[instruction]
                            if (dep != null && !dep.isEmpty()) {
                                println("Adding conditional edge: $insn to $successor")
                                conditionals[intArrayOf(insn, successor)] = instruction
                            }
                        }
                    } catch (e: Exception) {
                        System.err.println("Error creating control flow edge: " + e.message)
                        e.printStackTrace()
                    }
                }
            }

            println("Starting method analysis...")
            try {
                analyzer.analyze(internalClassName, toAnalyze)
            } catch (e: Exception) {
                System.err.println("Error during analysis: " + e.message)
                e.printStackTrace()
            }
            println("Analysis complete.")

            return IdentifyCall(instructions, sources, conditionals)
        }
    }
}
