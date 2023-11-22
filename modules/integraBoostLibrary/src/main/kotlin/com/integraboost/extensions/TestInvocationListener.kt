package com.integraboost.extensions

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.InvocationInterceptor
import org.junit.jupiter.api.extension.ReflectiveInvocationContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import java.util.concurrent.atomic.AtomicReference

/**
 * Wraps every test in runTest, to ensure you don't need
 * to remember to do this each time, in case a developer
 * forgets and accidentally causes memory leaks. This does
 * NOT cater for handling of lifecycle methods, where the same
 * thing may happen
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class TestInvocationListener: InvocationInterceptor {

    @Throws(Throwable::class)
    override fun interceptTestMethod(
        invocation: InvocationInterceptor.Invocation<Void>,
        invocationContext: ReflectiveInvocationContext<Method>,
        extensionContext: ExtensionContext,
    ) {
        val throwable = AtomicReference<Throwable>()
        LOGGER.info("[${this.javaClass.simpleName}] About to wrap test with a runTest")

        runTest(StandardTestDispatcher()) {
            try {
                LOGGER.info("[${this.javaClass.simpleName}] About to call test invocation")
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

    companion object {
        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}