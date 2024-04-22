package com.nophasenokill.setup.variations

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.extension.ExtensionContext.Store
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import java.io.RandomAccessFile
import java.nio.channels.FileLock
import java.nio.channels.OverlappingFileLockException
import java.nio.file.Files
import java.nio.file.Path

@ExtendWith(SharedAppExtension::class)
open class IntegrationTest {

    fun getGlobalNamespace(): ExtensionContext.Namespace = runBlocking  {
        return@runBlocking ExtensionContext.Namespace.GLOBAL
    }

    fun getStoreRoot(context: ExtensionContext): Store = runBlocking  {
        return@runBlocking SharedAppStore.getRoot(context)
    }

    companion object {
        var LOGGER: Logger = lazy { Logging.getLogger(SharedAppExtension.LOGGER_NAME) }.value
    }

}

object SharedAppStore {
    private val GLOBAL_NAMESPACE: ExtensionContext.Namespace = ExtensionContext.Namespace.GLOBAL

    fun getRoot(extensionContext: ExtensionContext): Store {
        return extensionContext.root.getStore(GLOBAL_NAMESPACE)
    }

    fun putObjectIntoGlobalStore(context: ExtensionContext, uniqueKey: Any, value: Any) = runBlocking  {
        context.root.getStore(GLOBAL_NAMESPACE).put(uniqueKey, value)
    }
}

object GlobalLock {
    // Needs to be something static, otherwise different temp directories may be created which initializes it multiple times
    private val lockFilePath = "/tmp/test-suite.lock"
    private val lockFile = RandomAccessFile(lockFilePath, "rw")
    private val fileChannel = lockFile.channel
    private var lock: FileLock? = null

    fun acquireLock(logger: Logger): Boolean {
        try {
            logger.lifecycle("File lock path is: ${lockFilePath}")
            lock = fileChannel.tryLock()
            return lock != null
        } catch (e: OverlappingFileLockException) {
            // ignore and do nothing
            lock
            return false
        }
    }

    fun releaseLock() {
        lock?.release()
    }
}

class SharedAppExtension:
    BeforeAllCallback,
    BeforeEachCallback,
    AfterAllCallback,
    AfterEachCallback,
    ParameterResolver,
    Store.CloseableResource {



    override fun beforeAll(context: ExtensionContext) = runBlocking  {

        val value = SharedAppStore.getRoot(context).get(SharedAppContextKey.TESTS_STARTED)

        if(value == null) {
            if (GlobalLock.acquireLock(IntegrationTest.LOGGER)) {
                try {
                    // Perform initialization only if the lock was acquired
                    SharedAppStore.putObjectIntoGlobalStore(context, SharedAppContextKey.TESTS_STARTED, this@SharedAppExtension)

                    IntegrationTest.LOGGER.lifecycle("Test suite initialization completed.")
                } finally {
                    GlobalLock.releaseLock()
                }
            }
        }
    }

    override fun beforeEach(context: ExtensionContext) = runBlocking  {
        IntegrationTest.LOGGER.lifecycle("beforeEach SharedAppExtension (integration tests) for test: {}", context.displayName)
    }

    override fun afterEach(context: ExtensionContext) = runBlocking {
        IntegrationTest.LOGGER.lifecycle("afterEach SharedAppExtension (integration tests) test: {}", context.displayName)
    }

    override fun afterAll(context: ExtensionContext) = runBlocking  {
        IntegrationTest.LOGGER.lifecycle("afterAll SharedAppExtension (integration tests) test: {}", context.displayName)
        IntegrationTest.LOGGER.lifecycle("Temp dir is:{} for {}", sharedRunnerDir.toString(), context.displayName)
    }

    /**
     * This should only be called at the end of all the project tests
     */
    override fun close() = runTest {
        /*
            Gradle mutates and changes the default logger, and when
            the test is the last test inside of the JVM fork -
            we need to get the delegate to ensure the test output
            reads in a way of which is an accurate reflection
            of the test executions.
         */
        when(val logger = IntegrationTest.LOGGER as org.slf4j.Logger) {
            is Logger -> logger.lifecycle("Finishing integration tests")
            else -> {
                val delegate = Logging.getLogger(LOGGER_NAME)
                delegate.lifecycle("Finishing integration tests")
            }
        }
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == ExtensionContext::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return extensionContext
    }



    companion object {
        val LOGGER_NAME = "SharedAppExtension"

        @field:TempDir(cleanup = CleanupMode.ON_SUCCESS)
        val sharedRunnerDir: Path = Files.createTempDirectory("shared-runner-dir")
    }
}

enum class SharedAppContextKey {
    TESTS_STARTED,
}