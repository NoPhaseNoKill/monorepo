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
}

fun main() {
    LearningsApp.main()
}
