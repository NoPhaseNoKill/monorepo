package com.nophasenokill.setup.junit.extensions

import com.nophasenokill.setup.logging.TestLogger
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.InvocationInterceptor
import org.junit.jupiter.api.extension.ReflectiveInvocationContext
import java.lang.reflect.Method
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Supplier
import kotlin.system.measureTimeMillis

/**
 * Wraps every test in runTest, to ensure you don't need
 * to remember to do this each time, in case a developer
 * forgets and accidentally causes memory leaks. This does
 * NOT cater for handling of lifecycle methods, where the same
 * thing may happen.
 *
 * It also adds the nicety of making the result(s) available immediately
 * in tests where they otherwise would have been delayed (via coroutines
 * or Thread.sleep's inside of a test run). See below example:
 *
 *      @Test
 *
 *      fun exampleTest() = runTest {
 *
 *          val deferred = async {
 *
 *              delay(1.seconds)
 *
 *              async {
 *                  delay(1.seconds)
 *              }.await()
 *          }
 *
 *          deferred.await() // result available immediately
 *      }
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class TestInvocationListener : InvocationInterceptor {

    @Throws(Throwable::class)
    override fun interceptTestMethod(
        invocation: InvocationInterceptor.Invocation<Void>,
        invocationContext: ReflectiveInvocationContext<Method>,
        extensionContext: ExtensionContext,
    ) {

        val throwable = AtomicReference<Throwable>()

        val time = measureTimeMillis {
            runTest(StandardTestDispatcher()) {
                try {
                    println("Test starting")
                    invocation.proceed()
                } catch (t: Throwable) {
                    throwable.set(t)
                }

                // caters for the behaviour where we need to re-throw the exception
                val t = throwable.get()
                if (t != null) {
                    throw t
                }
            }
        }
        println("Test took: ${time}ms")
    }
}