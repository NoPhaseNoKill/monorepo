package com.nophasenokill.setup.variations

import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.extension.ExtensionContext.Store
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import java.io.RandomAccessFile
import java.nio.channels.FileLock
import java.nio.channels.OverlappingFileLockException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@ExtendWith(SharedAppExtension::class)
open class IntegrationTest {

    fun getGlobalNamespace(): ExtensionContext.Namespace = runBlocking {
        return@runBlocking ExtensionContext.Namespace.GLOBAL
    }

    fun getStoreRoot(context: ExtensionContext): Store = runBlocking {
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

    fun getIOResult(context: ExtensionContext): String? {
        return getRoot(context).getOrDefault(SharedAppContextKey.IO_RESULT, String::class.java, null)
    }

    fun putObjectIntoGlobalStore(context: ExtensionContext, uniqueKey: Any, value: Any) = runBlocking {
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

@OptIn(ExperimentalCoroutinesApi::class, InternalCoroutinesApi::class)
class SharedAppExtension :
    BeforeAllCallback,
    BeforeEachCallback,
    AfterAllCallback,
    AfterEachCallback,
    ParameterResolver,
    Store.CloseableResource {


    private val testDispatcher = lazy { UnconfinedTestDispatcher() }
    private val scope = lazy { TestScope(testDispatcher.value) }
    private lateinit var globalCoroutineScope: CoroutineScope

    init {
        val testScope = scope.value
        testScope.runTest {
            globalCoroutineScope = this.backgroundScope
        }
    }

    sealed class AsyncTaskType
    object IOTask : AsyncTaskType()
    object NonIOTask : AsyncTaskType()


    suspend fun doAsyncTask(type: AsyncTaskType, block: suspend () -> Unit) {
        when (type) {
            IOTask -> {
                globalCoroutineScope.launch(Dispatchers.IO) {
                    block()
                }
            }

            NonIOTask -> {
                globalCoroutineScope.launch(globalCoroutineScope.coroutineContext) {
                    block()
                }
            }
        }
    }

    suspend fun <T> getAsyncTaskResult(type: AsyncTaskType, block: suspend () -> T): T {
        return try {
            val deferred = when (type) {
                IOTask -> {
                    globalCoroutineScope.async(Dispatchers.IO) {
                        block()
                    }
                }

                NonIOTask -> {
                    globalCoroutineScope.async(globalCoroutineScope.coroutineContext) {
                        block()
                    }
                }
            }
            deferred.await()
            deferred.getCompleted()
        } catch (e: CancellationException) {
            currentCoroutineContext().ensureActive() // throws if the current coroutine was cancelled
            IntegrationTest.LOGGER.lifecycle(e.message) // if this line executes, the exception is the result of `await` itself
            throw (e)
        }
    }


    suspend fun simulateIO(value: String, duration: Duration = 5.seconds): String {
        return try {
            withContext(Dispatchers.IO) {
                val result = globalCoroutineScope.async {
                    delay(duration)
                    value
                }
                result.await()
                result.getCompleted()
            }

        } catch (e: CancellationException) {
            currentCoroutineContext().ensureActive() // throws if the current coroutine was cancelled
            IntegrationTest.LOGGER.lifecycle(e.message) // if this line executes, the exception is the result of `await` itself
            throw (e)
        }
    }

    override fun beforeAll(context: ExtensionContext) {

        globalCoroutineScope.launch {
            try {
                val something = globalCoroutineScope.async {
                    val value = SharedAppStore.getRoot(context).get(SharedAppContextKey.TESTS_STARTED)
                    val isFirstMethodForTestClass = value == null

                    if (isFirstMethodForTestClass && GlobalLock.acquireLock(IntegrationTest.LOGGER)) {

                        // TODO FIX THIS
                        val ioResult = getAsyncTaskResult(IOTask) {
                            delay(5000) // Simulating blocking thread but time will be advanced properly
                            "someResult"
                        }

                        IntegrationTest.LOGGER.lifecycle("IOResult length: ${ioResult.length}")
                        return@async ioResult

                    } else {
                        IntegrationTest.LOGGER.lifecycle("Does it get Should be about 5 seconds")

                        // TODO FIX THIS

                        val deferred = getAsyncTaskResult(IOTask) {
                            delay(5000)
                            "polledValue"
                        }

                        try {
                            IntegrationTest.LOGGER.lifecycle("Should be about 5 seconds") // about five seconds
                            Assertions.assertEquals("polledValue", deferred )

                        } catch (e: CancellationException) {
                            currentCoroutineContext().ensureActive() // throws if the current coroutine was cancelled
                            IntegrationTest.LOGGER.lifecycle(e.message) // if this line executes, the exception is the result of `await` itself
                        }

                        // Poll until first gradle invocation finished
                        return@async checkIOResult(context)
                    }
                }.await()
            } catch (e: Exception) {

            } finally {
                GlobalLock.releaseLock()
            }
        }.onJoin
    }

    suspend fun checkIOResult(context: ExtensionContext) = coroutineScope {
        var value: String? = null
        var attempt = 0
        val maxAttempts = 10

        val deferred = async(Dispatchers.IO) {

            while (value == null && attempt < maxAttempts) {
                value = SharedAppStore.getIOResult(context) // Replace with actual implementation
                attempt++
                if (value == null) {
                    delay(5.seconds)
                }
            }

            return@async value
        }

        try {
            deferred.await()
            IntegrationTest.LOGGER.lifecycle("Should be about 5 seconds") // about five seconds
        } catch (e: CancellationException) {
            currentCoroutineContext().ensureActive() // throws if the current coroutine was cancelled
            IntegrationTest.LOGGER.lifecycle(e.message) // if this line executes, the exception is the result of `await` itself

        }

    }

    override fun beforeEach(context: ExtensionContext) = runBlocking {
        IntegrationTest.LOGGER.lifecycle(
            "beforeEach SharedAppExtension (integration tests) for test: {}",
            context.displayName
        )
    }

    override fun afterEach(context: ExtensionContext) = runBlocking {
        IntegrationTest.LOGGER.lifecycle(
            "afterEach SharedAppExtension (integration tests) test: {}",
            context.displayName
        )
    }

    override fun afterAll(context: ExtensionContext) = runBlocking {
        IntegrationTest.LOGGER.lifecycle(
            """
            afterAll SharedAppExtension (integration tests) test: {}
            Temp dir is: {}
        """.trimIndent(), context.displayName, sharedRunnerDir.toString()
        )
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
        when (val logger = IntegrationTest.LOGGER as org.slf4j.Logger) {
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
    TESTS_INITIALIZED,
    IO_RESULT,
    TEST_SUITE_SCOPE
}