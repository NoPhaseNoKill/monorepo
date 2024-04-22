package com.nophasenokill.setup.variations

import com.nophasenokill.setup.runner.SharedRunnerDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.gradle.api.logging.Logging
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
open class IntegrationTest {

    fun getGlobalNamespace(): ExtensionContext.Namespace {
        return ExtensionContext.Namespace.GLOBAL
    }

    fun getStoreRoot(context: ExtensionContext): Store {
        return SharedAppStore.getRoot(context)
    }

    fun getStartedWithContext(context: ExtensionContext): SingletonWithContext {
        return SingletonWithContext.start(context)
    }

    fun getContext2(context: ExtensionContext): ExtensionContext {
        return SingletonWithContext.start(context).context
    }

    init {
        Logging.getLogger("SharedAppExtension").lifecycle("SingletonIntegrationTest.started: ${SingletonIntegrationTest.started}")
        runTest {
            val started = SingletonIntegrationTest.start()
            // Logging.getLogger("SharedAppExtension").lifecycle("Initiatialising IntegrationTest. This is: $this")
            // Logging.getLogger("SharedAppExtension").lifecycle("Initiatialising IntegrationTest. store root: ${storeRoot}")
            // Logging.getLogger("SharedAppExtension").lifecycle("Initiatialising IntegrationTest. SingletonIntegrationTest: ${started}")
            // Logging.getLogger("SharedAppExtension").lifecycle("Initiatialising IntegrationTest. SingletonIntegrationTest started: ${started.started}")
            // Logging.getLogger("SharedAppExtension").lifecycle("Initiatialising IntegrationTest. SingletonIntegrationTest.started: ${SingletonIntegrationTest.started}")
            // Logging.getLogger("SharedAppExtension").lifecycle("Initiatialising IntegrationTest. SingletonWithContext: ${startedWithContext}")
            // Logging.getLogger("SharedAppExtension").lifecycle("Initiatialising IntegrationTest. SingletonWithContext context: ${startedWithContext.context}")
            // Logging.getLogger("SharedAppExtension").lifecycle("Initiatialising IntegrationTest. SingletonWithContext getContextSingleton: ${startedWithContext.getContextSingleton()}")
            // Logging.getLogger("SharedAppExtension").lifecycle("Initiatialising IntegrationTest. SingletonWithContext hashcode: ${startedWithContext.hashCode()}")
            // Logging.getLogger("SharedAppExtension").lifecycle("Initiatialising IntegrationTest. SingletonWithContext store: ${SingletonWithContext.store}")
            // Logging.getLogger("SharedAppExtension").lifecycle("Initiatialising IntegrationTest. context2: ${context2}")
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

    fun getRoot(extensionContext: ExtensionContext): Store {
        return extensionContext.root.getStore(GLOBAL_NAMESPACE)
    }

    fun putObjectIntoGlobalStore(context: ExtensionContext, uniqueKey: Any, value: Any) {
        context.root.getStore(GLOBAL_NAMESPACE).put(uniqueKey, value)
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
            Logging.getLogger("SharedAppExtension").lifecycle("File lock path is: ${lockFilePath}")
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

    @field:TempDir(cleanup = CleanupMode.ON_SUCCESS)
    val sharedRunnerDir: Path = Files.createTempDirectory("shared-runner-dir")





    override fun beforeAll(context: ExtensionContext) = runTest {
        if (GlobalLock.acquireLock()) {
            try {
                // Perform initialization only if the lock was acquired
                SharedAppStore.putObjectIntoGlobalStore(context, SharedAppContextKey.TESTS_STARTED, this@SharedAppExtension)
                Logging.getLogger("SharedAppExtension").lifecycle("Test suite initialization completed.")
            } finally {
                GlobalLock.releaseLock()
            }
        }
    }

    override fun beforeEach(context: ExtensionContext) = runTest {
        Logging.getLogger("SharedAppExtension").lifecycle("beforeEach SharedAppExtension (integration tests) for test: {}", context.displayName)
    }

    override fun afterEach(context: ExtensionContext) = runTest {
        Logging.getLogger("SharedAppExtension").lifecycle("afterEach SharedAppExtension (integration tests) test: {}", context.displayName)
    }

    override fun afterAll(context: ExtensionContext) = runTest {
        Logging.getLogger("SharedAppExtension").lifecycle("afterAll SharedAppExtension (integration tests) test: {}", context.displayName)
        Logging.getLogger("SharedAppExtension").lifecycle("Temp dir is:{} for {}", sharedRunnerDir.toString(), context.displayName)
    }

    /**
     * This should only be called at the end of all the project tests
     */
    override fun close() = runTest {
        Logging.getLogger("SharedAppExtension").lifecycle("Attempting to release lock")
        GlobalLock.releaseLock()
        Logging.getLogger("SharedAppExtension").lifecycle("Finishing integration tests")
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == ExtensionContext::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return extensionContext
    }

    fun storeRoot(context: ExtensionContext): Store {
        return SharedAppStore.getRoot(context)
    }

    companion object {
        val LOGGER: Logger = Logging.getLogger("SharedAppExtension")
    }
}

enum class SharedAppContextKey {
    TESTS_STARTED,
}