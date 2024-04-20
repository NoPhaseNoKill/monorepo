package com.nophasenokill.setup.junit.extensions


import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.extension.*


/*
    This currently gets invoked multiple time per test suite, which is not the intention.
    It's also a 'mostly' working example - but in future this should be fixed/looked
    at.
 */
class SharedTestSuiteExtension: BeforeAllCallback, BeforeEachCallback, AfterAllCallback, AfterEachCallback, ParameterResolver, ExtensionContext.Store.CloseableResource {

    override fun beforeAll(context: ExtensionContext) = runBlocking {
        val value = SharedTestSuiteStore.getRoot(context)
            .get(SharedTestSuiteContextKey.TESTS_STARTED)

        println("COMING INTO STANDALONE PLUGINS SharedTestSuiteExtension beforeAll. Value is: $value")

        if (value == null) {
            /*
                Adds the first invocation of the test container to the global store
                so that setupGlobalTestSuite and close are only applied once for the whole test run
                of the project
             */
            println("SETTING STANDALONE PLUGINS VALUE TO: ${this}")
            println("STANDALONE PLUGINS this@SharedTestSuiteExtension: ${this@SharedTestSuiteExtension}")
            SharedTestSuiteStore.putObjectIntoGlobalStore(context, SharedTestSuiteContextKey.TESTS_STARTED, this@SharedTestSuiteExtension)
            setupGlobalTestSuite()
        }

        setupTestClass()
    }

    /**
     * This should only be called at the start of all the project tests
     */

    fun setupGlobalTestSuite()  = runBlocking {

    }


    /**
     * This should only be called at the start of each test class (file)
     */
    fun setupTestClass()  = runBlocking  {

    }


    override fun beforeEach(context: ExtensionContext)  = runBlocking {

    }

    override fun afterEach(context: ExtensionContext)  = runBlocking {

    }

    override fun afterAll(context: ExtensionContext)  = runBlocking {

    }

    /**
     * This should only be called at the end of all the project tests
     */
    override fun close()  = runBlocking {

    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean  = runBlocking {
        return@runBlocking parameterContext.parameter.type == ExtensionContext::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any  = runBlocking {
        return@runBlocking extensionContext
    }
}