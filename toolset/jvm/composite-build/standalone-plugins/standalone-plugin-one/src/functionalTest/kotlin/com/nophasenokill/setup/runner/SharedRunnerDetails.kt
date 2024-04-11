package com.nophasenokill.setup.runner

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
        suspend fun getRunner(sharedRunnerDir: File, ): GradleRunner {

            return withContext(Dispatchers.IO) {

                val runner = DefaultGradleRunner()
                    .withProjectDir(sharedRunnerDir)
                    .withPluginClasspath()


                return@withContext runner

            }
        }
    }
}