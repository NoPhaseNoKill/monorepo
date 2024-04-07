package com.nophasenokill.extensions


import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.*


/*
    This currently gets invoked multiple time per test suite, which is not the intention.
    It's also a 'mostly' working example - but in future this should be fixed/looked
    at.
 */
class SharedTestSuiteExtension: BeforeAllCallback, BeforeEachCallback, AfterAllCallback, AfterEachCallback, ParameterResolver, ExtensionContext.Store.CloseableResource {


    override fun beforeAll(context: ExtensionContext) = runTest {
        val value = SharedTestSuiteStore
            .getRoot(context)
            .get(SharedTestSuiteContextKey.TESTS_STARTED)

        if (value == null) {
            /*
                Adds the first invocation of the test container to the global store
                so that setupGlobalTestSuite and close are only applied once for the whole test run
                of the project
             */
            SharedTestSuiteStore.putObjectIntoGlobalStore(context, SharedTestSuiteContextKey.TESTS_STARTED, this)
            setupGlobalTestSuite()
        }

        setupTestClass()
    }

    /**
     * This should only be called at the start of all the project tests
     */

    fun setupGlobalTestSuite() = runTest {

    }


    /**
     * This should only be called at the start of each test class (file)
     */
    fun setupTestClass() = runTest {

    }


    override fun beforeEach(context: ExtensionContext) = runTest {
        println("Unique id for test is: ${context.uniqueId}")
    }

    override fun afterEach(context: ExtensionContext) = runTest {

    }

    override fun afterAll(context: ExtensionContext) = runTest {

    }

    /**
     * This should only be called at the end of all the project tests
     */
    override fun close() = runTest {

    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == ExtensionContext::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return extensionContext
    }
}