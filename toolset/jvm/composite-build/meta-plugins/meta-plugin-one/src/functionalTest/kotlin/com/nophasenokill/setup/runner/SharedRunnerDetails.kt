package com.nophasenokill.setup.runner

import com.nophasenokill.setup.variations.TestDirectory

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.internal.DefaultGradleRunner
import java.io.File

data class SharedRunnerDetails(
    val projectDir: File,
    val buildFile: File,
    val settingsFile: File,
    val gradleRunner: GradleRunner
) {
    object SharedRunner {
        fun getRunner(testDirectory: TestDirectory): GradleRunner {
            return DefaultGradleRunner()
                .withJvmArguments("-Xmx2g", "-XX:MaxMetaspaceSize=384m")
                .withProjectDir(testDirectory.mainDirectory.toFile())
                .withPluginClasspath()
        }
    }
}