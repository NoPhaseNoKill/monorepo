package com.nophasenokill.tasks.asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.analysis.AnalyzerException
import org.objectweb.asm.util.Textifier
import org.objectweb.asm.util.TraceMethodVisitor
import java.io.IOException
import java.lang.invoke.MethodHandles
import java.util.*

class IdentifyCallExample {
    fun toAnalyze() {
        (if (Math.random() > 0.5) System.out else System.err).println(
            if (anotherMethod1(
                    if (anotherMethod2("a", "b")) "c" else if (anotherMethod3("d", "e")) "f" else "g",
                    if (anotherMethod4("h", "i")) "j" else "k"
                )
            ) "l" else "m"
        )
    }

    class Level(b: IntArray) {
        var lo: Int
        var hi: Int
        var branches: ArrayDeque<IntArray> = ArrayDeque()

        init {
            lo = b[0]
            hi = b[1]
            branches.add(b)
        }

        fun insert(b: IntArray): Boolean {
            if (b[1] <= lo) {
                branches.addFirst(b)
                lo = b[0]
            } else if (b[0] >= hi) {
                branches.addLast(b)
                hi = b[1]
            } else return if ((b[0] > lo && b[1] < hi) && (b[0] + b[1] shr 1) > ((lo + hi) shr 1)) tryTail(
                b,
                lo,
                hi
            ) else tryHead(b, lo, hi)
            return true
        }

        private fun tryHead(b: IntArray, lo: Int, hi: Int): Boolean {
            var lo = lo
            val head = branches.removeFirst()
            try {
                if (head[1] > b[0]) return false
                if (branches.isEmpty() || (branches.first[0].also { lo = it }) >= b[1]) {
                    branches.addFirst(b)
                    return true
                } else return if ((b[0] > lo && b[1] < hi) && (b[0] + b[1] shr 1) > ((lo + hi) shr 1)) tryTail(
                    b,
                    lo,
                    hi
                ) else tryHead(b, lo, hi)
            } finally {
                branches.addFirst(head)
            }
        }

        private fun tryTail(b: IntArray, lo: Int, hi: Int): Boolean {
            var hi = hi
            val tail = branches.removeLast()
            try {
                if (tail[0] < b[1]) return false
                if (branches.isEmpty() || (branches.last[1].also { hi = it }) <= b[0]) {
                    branches.addLast(b)
                    return true
                } else return if (b[0] > lo && b[1] < hi && (b[0] + b[1]) shr 1 > ((lo + hi) shr 1)) tryTail(
                    b,
                    lo,
                    hi
                ) else tryHead(b, lo, hi)
            } finally {
                branches.addLast(tail)
            }
        }
    }

    companion object {
        @JvmStatic
        fun anotherMethod1(str: String?, oof: String?): Boolean {
            return true
        }

        @JvmStatic
        fun anotherMethod2(str: String?, oof: String?): Boolean {
            return true
        }

        @JvmStatic
        fun anotherMethod3(str: String?, oof: String?): Boolean {
            return true
        }

        @JvmStatic
        fun anotherMethod4(str: String?, oof: String?): Boolean {
            return true
        }
    }
}
