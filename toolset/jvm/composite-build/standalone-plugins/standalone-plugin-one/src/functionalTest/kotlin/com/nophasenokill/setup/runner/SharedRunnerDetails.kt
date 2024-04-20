package com.nophasenokill.setup.runner

import kotlinx.coroutines.runBlocking
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
        fun getRunner(sharedRunnerDir: File, ): GradleRunner = runBlocking {
            return@runBlocking DefaultGradleRunner()
                .withJvmArguments("-Xmx2g", "-XX:MaxMetaspaceSize=384m")
                .withProjectDir(sharedRunnerDir)
                .withPluginClasspath()
        }
    }
}