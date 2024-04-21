package com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin.someNewDir2

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestCoroutineExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SomeTest1 {

    @Test
    fun doSomething() {
        println("Test running ${this.javaClass.name}")
    }

    @Test
    fun testExampleBackgroundJob() = runTest {
        val channel = Channel<Int>()
        backgroundScope.launch {
            var i = 0
            while (true) {
                if(i == 99) {
                    println("Ohai")
                }
                channel.send(i++)
            }
        }
        repeat(100) {
            assertEquals(it, channel.receive())
        }.also {
            println("Ohai2")
        }

    }
}

@ExperimentalCoroutinesApi
class MyCoroutineTest : CoroutineTest2 {

    override lateinit var runTestScope: EnhancedRunTestScope

    private val stateFlow = MutableStateFlow(false)

    @Test
    fun `my test`() = runTestScope {

        stateFlow.emit(true)
        assertEquals(true, stateFlow.value)

        val channel = Channel<Int>()
        backgroundScope.launch {
            var i = 0
            while (true) {
                if(i == 99) {
                    println("Ohai")
                }
                channel.send(i++)
            }
        }
        repeat(100) {
            assertEquals(it, channel.receive())
        }.also {
            delay(100)
            println("Ohai2")
        }
    }
}