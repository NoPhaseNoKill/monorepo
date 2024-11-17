package com.nophasenokill.basics

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.internal.os.OperatingSystem
import javax.inject.Inject


abstract class BuildEnvironmentService : BuildService<BuildEnvironmentService.Parameters> {

    interface Parameters : BuildServiceParameters {
        val rootProjectDir: DirectoryProperty
        val rootProjectBuildDir: DirectoryProperty
    }

    @get:Inject
    abstract val providers: ProviderFactory

    val gitCommitId = git("rev-parse", "HEAD")
    val gitBranch = git("rev-parse", "--abbrev-ref", "HEAD")

    @Suppress("UnstableApiUsage")
    private
    fun git(vararg args: String): Provider<String> {
        val projectDir = parameters.rootProjectDir.asFile.get()
        val execOutput = providers.exec {
            workingDir = projectDir
            isIgnoreExitValue = true
            commandLine = listOf("git", *args)
            if (OperatingSystem.current().isWindows) {
                commandLine = listOf("cmd.exe", "/d", "/c") + commandLine
            }
        }
        return execOutput.result.zip(execOutput.standardOutput.asText) { result, outputText ->
            if (result.exitValue == 0) outputText.trim()
            else "<unknown>" // It's a source distribution, we don't know.
        }
    }
}
