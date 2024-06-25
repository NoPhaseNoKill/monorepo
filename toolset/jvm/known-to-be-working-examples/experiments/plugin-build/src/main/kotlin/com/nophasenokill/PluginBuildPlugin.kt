package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginBuildPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        project.apply {
            plugin("org.gradle.kotlin.kotlin-dsl")
        }

        project.dependencies.apply {
            gradleApi()
            gradleTestKit()

            add("testImplementation", listOf(mapOf("group" to "org.junit.jupiter", "module" to "junit-jupiter", "version" to "5.10.1")))
            add("testRuntimeOnly", listOf(mapOf("group" to "org.junit.jupiter", "module" to "junit-platform-launcher")))
        }
    }
}