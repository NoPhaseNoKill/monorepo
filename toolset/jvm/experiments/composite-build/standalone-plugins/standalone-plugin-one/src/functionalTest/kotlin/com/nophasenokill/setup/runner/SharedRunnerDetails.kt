package com.nophasenokill.setup.runner


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
        fun getRunner(sharedRunnerDir: File, ): GradleRunner {
            return DefaultGradleRunner()
                .withJvmArguments("-Xmx2g", "-XX:MaxMetaspaceSize=384m")
                .withProjectDir(sharedRunnerDir)
                .withPluginClasspath()
        }

        fun getRunner2(sharedRunnerDir: File, withPluginClasspath: List<File> ): GradleRunner {
            return DefaultGradleRunner()
                .withJvmArguments("-Xmx2g", "-XX:MaxMetaspaceSize=384m")
                .withProjectDir(sharedRunnerDir)
                .withPluginClasspath(withPluginClasspath)
        }
    }
}