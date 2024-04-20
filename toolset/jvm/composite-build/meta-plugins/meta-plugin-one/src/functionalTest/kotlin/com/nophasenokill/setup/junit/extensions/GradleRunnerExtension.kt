package com.nophasenokill.setup.junit.extensions

import com.nophasenokill.setup.variations.TestDirectory
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.io.CleanupMode
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path

class GradleRunnerExtension: BeforeAllCallback, BeforeEachCallback, ParameterResolver {

    @field:TempDir(cleanup = CleanupMode.ON_SUCCESS)
    val sharedRunnerDir: Path = Files.createTempDirectory("shared-runner-dir")

    override fun beforeAll(context: ExtensionContext) {
        val value = SharedTestSuiteStore.getRoot(context).get(SharedTestSuiteContextKey.TESTS_STARTED)
        if (value == null) {
            SharedTestSuiteStore.putObjectIntoGlobalStore(
                context,
                SharedTestSuiteContextKey.TESTS_STARTED,
                this@GradleRunnerExtension
            )
            setupGlobalTestSuite()
        }
    }

    override fun beforeEach(context: ExtensionContext) {
        val tempDir = Files.createDirectory(Path("$sharedRunnerDir/${context.displayName}-"))
        val testKitDir = Files.createDirectory(Path("$sharedRunnerDir/test-kit-dir"))
        val testDirectory = TestDirectory(context.displayName, tempDir, testKitDir)
        SharedTestSuiteStore.putObjectIntoGlobalStore(context, SharedTestSuiteContextKey.TEST_DIRECTORIES, testDirectory)
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == ExtensionContext::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return extensionContext
    }

    fun setupGlobalTestSuite() {

    }

}