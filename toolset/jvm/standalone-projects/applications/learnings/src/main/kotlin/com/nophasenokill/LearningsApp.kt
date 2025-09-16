package com.nophasenokill


object LearningsApp {
    fun main() {
        println("Hello world!")
    }

    fun printLinkedList(head: Node) {
        var current: Node? = head

        while(current !== null) {
            println(current.value)
            current = current.next
        }
    }

    fun printLinkedListRecursive(head: Node?) {
        if(head === null) return

        println(head.value)
        printLinkedListRecursive(head.next)
    }

    fun printLinkedListToArray(head: Node) {
        val values: MutableList<Any> = mutableListOf()
        addLinkedListToArray(head, values)
        println(values)
    }

    fun printLinkedListToArrayRecursively(head: Node) {
        val values: MutableList<Any> = mutableListOf()
        addLinkedListToArrayRecursively(head, values)
        println(values)
    }

    private fun addLinkedListToArrayRecursively(head: Node?, values: MutableList<Any>) {
        if(head === null) return

        values.add(head.value)
        addLinkedListToArrayRecursively(head.next, values)
    }

    private fun addLinkedListToArray(head: Node?, values: MutableList<Any>) {
        var current: Node? = head

        while(current !== null) {
            values.add(current.value)
            current = current.next
        }
    }
}

fun main() {
    LearningsApp.main()
}
