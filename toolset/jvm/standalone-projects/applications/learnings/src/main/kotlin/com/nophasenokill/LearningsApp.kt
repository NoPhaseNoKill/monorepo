package com.nophasenokill


object LearningsApp {
    fun main() {
        println("Hello world!")
    }

    fun printLinkedList() {
        val head = setupLinkedList()

        var current: Node? = head

        while(current !== null) {
            println(current.value)
            current = current.next
        }
    }
}

fun main() {
    LearningsApp.main()
}


// Represents the equivalent of: A -> B -> C -> D
private fun setupLinkedList(): Node {
    val a = Node("A")
    val b = Node("B")
    val c = Node("C")
    val d = Node("D")

    a.next = b
    b.next = c
    c.next = d

    return a
}
