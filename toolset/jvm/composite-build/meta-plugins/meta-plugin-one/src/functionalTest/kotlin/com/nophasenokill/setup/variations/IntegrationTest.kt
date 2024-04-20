package com.nophasenokill.setup.variations

import com.nophasenokill.setup.runner.SharedRunnerDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.gradle.testkit.runner.BuildResult
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.extension.ExtensionContext.Store
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileLock
import java.nio.channels.OverlappingFileLockException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.properties.Delegates

@ExtendWith(SharedAppExtension::class)

object SingletonTest
object SingletonWithContext {
    @JvmStatic
    lateinit var context: ExtensionContext

    @JvmStatic
    lateinit var store: Store

    fun start(extensionContext: ExtensionContext): SingletonWithContext {
        context = extensionContext
        store = SharedAppStore.getRoot(context)
        return this
    }

    fun getContextSingleton(): ExtensionContext {
        return this.context
    }
}
object SingletonIntegrationTest {

    val mutex = Mutex()
    var hasStarted = false

    suspend fun setStarted() {
        mutex.withLock {
            hasStarted = true
        }
    }

    suspend fun checkIfStarted(): Boolean {
        return mutex.withLock {
            hasStarted
        }
    }

    var started by Delegates.notNull<Boolean>()

    init {
        started = false
    }

    suspend fun start(): SingletonIntegrationTest {
        setStarted()
        started = true
        return this
    }
}

@ExtendWith(SharedAppExtension::class)
open class IntegrationTest(context: ExtensionContext) {

    val storeRoot = SharedAppStore.getRoot(context)


    val startedWithContext = SingletonWithContext.start(context)
    val context2 = SingletonWithContext.start(context).context

    init {
        println("SingletonIntegrationTest.started: ${SingletonIntegrationTest.started}")
        runTest {
            val started = SingletonIntegrationTest.start()
            println("Initiatialising IntegrationTest. This is: $this")
            println("Initiatialising IntegrationTest. store root: ${storeRoot}")
            println("Initiatialising IntegrationTest. SingletonIntegrationTest: ${started}")
            println("Initiatialising IntegrationTest. SingletonIntegrationTest started: ${started.started}")
            println("Initiatialising IntegrationTest. SingletonIntegrationTest.started: ${SingletonIntegrationTest.started}")
            println("Initiatialising IntegrationTest. SingletonWithContext: ${startedWithContext}")
            println("Initiatialising IntegrationTest. SingletonWithContext context: ${startedWithContext.context}")
            println("Initiatialising IntegrationTest. SingletonWithContext getContextSingleton: ${startedWithContext.getContextSingleton()}")
            println("Initiatialising IntegrationTest. SingletonWithContext hashcode: ${startedWithContext.hashCode()}")
            println("Initiatialising IntegrationTest. SingletonWithContext store: ${SingletonWithContext.store}")
            println("Initiatialising IntegrationTest. context2: ${context2}")
        }
    }

    fun runExpectedSuccessTask(details: SharedRunnerDetails, task: String): BuildResult {
        return details.gradleRunner.withArguments(task, "--warning-mode=all").build()
    }

    suspend fun createFile(file: File) {
        withContext(Dispatchers.IO) {
            file.createNewFile()
        }
    }

    suspend fun writeText(file: File, textBlock:() -> String) {
        /*
            Putting this outside of the IO context ensures that if the text block
            is computationally expensive we avoid putting this on the IO thread
         */
        val text = textBlock()
        withContext(Dispatchers.IO) {
            file.writeText(text)
        }
    }

    suspend fun appendText(file: File, textBlock:() -> String) {
        /*
            Putting this outside of the IO context ensures that if the text block
            is computationally expensive we avoid putting this on the IO thread
         */
        val text = textBlock()
        withContext(Dispatchers.IO) {
            file.appendText(text)
        }
    }

    suspend fun readLines(buildResult: BuildResult): List<String> {
        return withContext(Dispatchers.IO) {
            return@withContext buildResult.output.lines()
        }
    }
}

object SharedAppStore {
    private val GLOBAL_NAMESPACE: ExtensionContext.Namespace = ExtensionContext.Namespace.GLOBAL

    fun getRoot(extensionContext: ExtensionContext): ExtensionContext.Store {
        return extensionContext.root.getStore(GLOBAL_NAMESPACE)
    }

    fun putObjectIntoGlobalStore(context: ExtensionContext, uniqueKey: Any, value: Any) {
        context.root.getStore(GLOBAL_NAMESPACE).put(uniqueKey, value)
    }
}

object SingletonIntegrationTestNew {
    private val mutex = Mutex()
    @Volatile
    private var started = false

    suspend fun checkIfStarted(): Boolean {
        mutex.withLock {
            return started
        }
    }

    suspend fun setStarted() {
        mutex.withLock {
            if (!started) {
                started = true
                // Perform initialization here
                println("Initializing the test suite...")
            }
        }
    }
}

object GlobalLock {
    // Needs to be something static, otherwise different temp directories may be created which initializes it multiple times
    private val lockFilePath = "/tmp/test-suite.lock"
    private val lockFile = RandomAccessFile(lockFilePath, "rw")
    private val fileChannel = lockFile.channel
    private var lock: FileLock? = null

    fun acquireLock(): Boolean {
        try {
            println("File lock path is: ${lockFilePath}")
            lock = fileChannel.tryLock()
            return lock != null
        } catch (e: OverlappingFileLockException) {
            // ignore and do nothing
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
    ExtensionContext.Store.CloseableResource {

    override fun beforeAll(context: ExtensionContext) = runTest {
        if (GlobalLock.acquireLock()) {
            try {
                // Perform initialization only if the lock was acquired
                println("Test suite initialization completed.")
            } finally {

            }
        }
    }

    override fun beforeEach(context: ExtensionContext) {
        println("beforeEach SharedAppExtension (integration tests) for test: ${context.displayName}")
    }

    override fun afterEach(context: ExtensionContext) {
        println("afterEach SharedAppExtension (integration tests) test: ${context.displayName}")
    }

    override fun afterAll(context: ExtensionContext) {
        println("afterAll SharedAppExtension (integration tests) test: ${context.displayName}")
        println("Temp dir is: $sharedRunnerDir for ${context.displayName}")
    }

    /**
     * This should only be called at the end of all the project tests
     */
    override fun close() {
        GlobalLock.releaseLock()
        println("Finishing integration tests")
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == ExtensionContext::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return extensionContext
    }


    companion object {

        @JvmStatic
        val GLOBAL_NAMESPACE: ExtensionContext.Namespace = ExtensionContext.Namespace.GLOBAL

        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(this::class.java.declaringClass)

        @field:TempDir(cleanup = CleanupMode.ON_SUCCESS)
        val sharedRunnerDir: Path = Files.createTempDirectory("shared-runner-dir")

        @JvmStatic
        val storeRoot = fun(context: ExtensionContext): ExtensionContext.Store {
            return SharedAppStore.getRoot(context)
        }
    }
}

enum class SharedAppContextKey {
    TESTS_STARTED,
}