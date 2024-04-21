package com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin.someNewDir2

import com.nophasenokill.setup.variations.SharedAppContextKey
import com.nophasenokill.setup.variations.SharedAppStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource
import java.io.File
import java.io.FileWriter
import java.io.Writer
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestCoroutineExtension::class)
interface CoroutineTest {
    var testScope: TestScope
    var dispatcher: TestDispatcher
}

@ExperimentalCoroutinesApi
@ExtendWith(CoroutineTestExtension::class)
interface CoroutineTest2 {
    var runTestScope: EnhancedRunTestScope
}

typealias EnhancedTestScope = TestScope

typealias TestBody = suspend EnhancedTestScope.() -> Unit

typealias RunTestEquivalent = (
    testBody: TestBody
) -> TestResult


typealias EnhancedRunTestScope = RunTestEquivalent

@ExperimentalCoroutinesApi
class CoroutineTestExtension : TestInstancePostProcessor, BeforeAllCallback, AfterAllCallback {

    private val unconfinedTestDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(unconfinedTestDispatcher)
    private val runTestScope: EnhancedRunTestScope = { testBody: TestBody ->
        testScope.runTest {
            val timeBefore = currentTime
            val timeBeforeActual = System.currentTimeMillis()
            println("Time before is: ${formatMillis(timeBeforeActual)}")

            testBody()

            advanceUntilIdle()
            val timeAfter = currentTime
            val timeAfterActual = System.currentTimeMillis()
            println("Time after is: ${formatMillis(timeAfterActual)}")
            val timeTestTook = timeAfterActual- timeBeforeActual
            val timeAdvancedBy = timeAfter - timeBefore
            val timeSaved = if(timeAdvancedBy > 0) timeAdvancedBy else 0

            /*
                 100 + 18
                ___________ * 100 = 118/18 * 100 = 6.55x slower
                    18
             */

            val timeSavedPercentage = BigDecimal((timeSaved + timeTestTook).times(100).div(timeTestTook)).setScale(2).div(BigDecimal(100))

            println("""
            Test took: $timeTestTook ms
            Time saved: $timeSaved ms
            Execution would have normally been ${timeSavedPercentage}x slower
        """.trimIndent())
        }
    }

    override fun postProcessTestInstance(testInstance: Any?, context: ExtensionContext?) {
        (testInstance as? CoroutineTest2)?.let { coroutineTest ->
            coroutineTest.runTestScope = runTestScope
        }
    }

    override fun beforeAll(context: ExtensionContext?) {
        Dispatchers.setMain(unconfinedTestDispatcher)
    }

    override fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }

    fun formatMillis(millis: Long): String {
        val instant = Instant.ofEpochMilli(millis)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        return localDateTime.format(formatter)
    }
}

/**
 * JUnit 5 Extension for automatically creating a [UnconfinedTestDispatcher],
 * then a [TestScope] with the same CoroutineContext.
 * */
@ExperimentalCoroutinesApi
@OptIn(ExperimentalCoroutinesApi::class)
class TestCoroutineExtension : TestInstancePostProcessor, BeforeAllCallback, AfterAllCallback {

    private val dispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(dispatcher)

    override fun postProcessTestInstance(testInstance: Any?, context: ExtensionContext?) {
        (testInstance as? CoroutineTest)?.let { coroutineTest ->
            coroutineTest.testScope = testScope
            coroutineTest.dispatcher = dispatcher
        }
    }

    override fun beforeAll(context: ExtensionContext?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}

class SomeExtension: AfterAllCallback, CloseableResource {

    lateinit var writer: Writer

    override fun afterAll(context: ExtensionContext) = runTest {
        writer = FileWriter.nullWriter()
        println("After all for ${context.displayName}")
        // Thread.sleep(1000)
        SharedAppStore.putObjectIntoGlobalStore(context, SharedAppContextKey.TESTS_STARTED, this@SomeExtension)
        return@runTest
    }

    override fun close() {

        /*

        // TODO WHERE IS MISSING 253???

            Gradle Test Executor 254 STANDARD_OUT
            HELLOOOOOOOOOOOOOO
            Closing

            SomeTest2 PASSED

            Gradle Test Executor 252 STANDARD_OUT
                HELLOOOOOOOOOOOOOO
                Closing

            SomeTest1 PASSED

            Gradle Test Executor 251 STANDARD_OUT
                HELLOOOOOOOOOOOOOO
                Closing

         */

        writer.close()
        println("HELLOOOOOOOOOOOOOO")
        println("Closing")
    }
}