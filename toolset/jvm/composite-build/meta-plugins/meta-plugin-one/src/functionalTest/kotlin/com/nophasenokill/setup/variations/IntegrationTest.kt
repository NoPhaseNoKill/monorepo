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

@ExtendWith(SharedAppExtension::class)
open class IntegrationTest(context: ExtensionContext) {

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

object GlobalLock: CloseableResource {
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

    override fun close() {
        try {
            lock?.close()
            lockFile.channel.close()
            File(lockFilePath).delete()
        } catch (e: Exception) {
            println("Exception while attempting to close lock: ${e.message}")
        }

        println("Lock file channel is open: ${lockFile.channel.isOpen}")
        println("Lock file exists: ${File(lockFilePath).exists()}")

        var hasLockBeenReleased = false

        try {
            lock?.release()
        } catch(e: ClosedChannelException) {
            hasLockBeenReleased = true

        }

        println("Lock has been released: $hasLockBeenReleased")
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
        GlobalLock.close()
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