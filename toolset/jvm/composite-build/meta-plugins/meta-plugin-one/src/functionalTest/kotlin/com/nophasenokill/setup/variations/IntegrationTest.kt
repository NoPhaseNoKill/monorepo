package com.nophasenokill.setup.variations

import com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin.someNewDir2.CoroutineTest2
import com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin.someNewDir2.EnhancedRunTestScope
import com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin.someNewDir2.EnhancedTestScope
import com.nophasenokill.setup.junit.extensions.GradleRunnerExtension
import com.nophasenokill.setup.junit.extensions.SharedTestSuiteStore
import com.nophasenokill.setup.runner.SharedRunnerDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.gradle.testkit.runner.BuildResult
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.extension.ExtensionContext.Store
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.ClosedChannelException
import java.nio.channels.FileLock
import java.nio.channels.OverlappingFileLockException
import java.nio.file.Files
import java.nio.file.Path


@ExperimentalCoroutinesApi
open class IntegrationTest: CoroutineTest2 {

    override lateinit var runTestScope: EnhancedRunTestScope

    fun getRunTestScope(context: ExtensionContext): EnhancedRunTestScope {
        return SharedTestSuiteStore.getRunTestScope(context)
    }

    fun runExpectedSuccessTask(details: SharedRunnerDetails, task: String): BuildResult {
        return details.gradleRunner.withArguments(task, "--warning-mode=all").build()
    }

    fun EnhancedTestScope.createFile(file: File) {
        file.createNewFile()
    }

    suspend fun createFile(file: File) {
        file.createNewFile()
    }

    suspend fun writeText(file: File, textBlock:() -> String) {
        /*
            Putting this outside of the IO context ensures that if the text block
            is computationally expensive we avoid putting this on the IO thread
         */
        val text = textBlock()

            file.writeText(text)

    }

    suspend fun appendText(file: File, textBlock:() -> String) {
        /*
            Putting this outside of the IO context ensures that if the text block
            is computationally expensive we avoid putting this on the IO thread
         */
        val text = textBlock()

            file.appendText(text)

    }

    suspend fun readLines(buildResult: BuildResult): List<String> {
        return buildResult.output.lines()
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

object GlobalLock {
    // Needs to be something static, otherwise different temp directories may be created which initializes it multiple times
    private val lockFilePath = "/tmp/test-suite.lock"
    private val lockFile = RandomAccessFile(lockFilePath, "rw")
    private var lock: FileLock? = null

    /*
        Need to be very careful in that we close this, as it's open for the whole suite
     */
    fun acquireLock(): Boolean {
        try {
            println("File lock path is: ${lockFilePath}")
            lock = lockFile.channel.tryLock()
            return lock != null
        } catch (e: OverlappingFileLockException) {
            // ignore and do nothing
            return false
        }
    }

    fun releaseLock() {
        lock?.release()
    }

    fun getLock(): FileLock? {
        return lock
    }

    fun getLockFilePath(): String {
        return lockFilePath
    }

    fun getLockFile(): RandomAccessFile {
        return lockFile
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
            // Perform initialization only if the lock was acquired
            SharedAppStore.putObjectIntoGlobalStore(context, SharedAppContextKey.TESTS_STARTED, this@SharedAppExtension)
            println("Test suite initialization completed.")
        }
    }

    override fun beforeEach(context: ExtensionContext) = runTest {
        println("beforeEach SharedAppExtension (integration tests) for test: ${context.displayName}")
    }

    override fun afterEach(context: ExtensionContext) = runTest {
        println("afterEach SharedAppExtension (integration tests) test: ${context.displayName}")
    }

    override fun afterAll(context: ExtensionContext) = runTest {
        println("afterAll SharedAppExtension (integration tests) test: ${context.displayName}")
        println("Temp dir is: $sharedRunnerDir for ${context.displayName}")
    }

    /**
     * This should only be called at the end of all the project tests
     */
    override fun close() = runTest {
        try {
            GlobalLock.getLock()?.close()
            GlobalLock.getLockFile().channel.close()
            File(GlobalLock.getLockFilePath()).delete()
        } catch (e: Exception) {
            LOGGER.info("Exception while attempting to close lock: ${e.message}")
        }

        println("Lock file channel is open: ${GlobalLock.getLockFile().channel.isOpen}")
        println("Lock file exists: ${File(GlobalLock.getLockFilePath()).exists()}")

        var hasLockBeenReleased = false

        try {
            GlobalLock.getLock()?.release()
        } catch(e: ClosedChannelException) {
            hasLockBeenReleased = true
        }

        println("Lock has been released: $hasLockBeenReleased")
        println("Finishing integration tests.")
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