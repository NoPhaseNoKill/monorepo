package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import java.io.File

class ComponentPlugin: Plugin<Settings> {
    override fun apply(settings: Settings) {

        settings.run {

            settings.pluginManagement.repositories.gradlePluginPortal()
            settings.dependencyResolutionManagement.repositories.mavenCentral()

            fun includeBuildsAndProjects(dir: File) {
                dir.walk().forEach { file ->
                    if (file.isFile && file.name == "build.gradle.kts") {
                        // Include any projects
                        val relativePath = file.parentFile.relativeTo(rootDir).path.replace(File.separator, ":")
                        include(relativePath)
                        project(":$relativePath").projectDir = file.parentFile
                    } else if (file.isFile && file.name == "settings.gradle.kts") {
                        // Include any builds
                        includeBuild(file.parentFile)
                    }
                }
            }



            includeBuildsAndProjects(rootDir)
            enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
            enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
        }
    }
}