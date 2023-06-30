package com.integraboost.extensions

import com.integraboost.domain.SharedAppContextKey
import com.integraboost.store.SharedAppStore
import org.junit.jupiter.api.extension.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SharedAppExtension: BeforeAllCallback, BeforeEachCallback, AfterAllCallback, AfterEachCallback, ParameterResolver, ExtensionContext.Store.CloseableResource {

    override fun beforeAll(context: ExtensionContext) {
        val value = SharedAppStore
            .getRoot(context)
            .get(SharedAppContextKey.TESTS_STARTED)

        if (value == null) {
            /*
                Adds the first invocation of the test container to the global store
                so that setupSharedContext and close are only applied once for the whole test run
                of the project
             */
            LOGGER.debug("Starting integration tests")
            SharedAppStore.putObjectIntoGlobalStore(context, SharedAppContextKey.TESTS_STARTED, this)
        }
    }

    override fun beforeEach(context: ExtensionContext) {
        LOGGER.debug("beforeEach SharedAppExtension (integration tests) for test: ${context.displayName}")
    }

    override fun afterEach(context: ExtensionContext) {
        LOGGER.debug("afterEach SharedAppExtension (integration tests) test: ${context.displayName}")
    }

    override fun afterAll(context: ExtensionContext) {
        LOGGER.debug("afterAll SharedAppExtension (integration tests) test: ${context.displayName}")
    }

    /**
     * This should only be called at the end of all the project tests
     */
    override fun close() {
        LOGGER.debug("Finishing integration tests")
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == ExtensionContext::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return extensionContext
    }

    companion object {
        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}