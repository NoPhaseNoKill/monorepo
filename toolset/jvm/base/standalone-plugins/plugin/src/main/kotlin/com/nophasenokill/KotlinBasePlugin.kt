package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar


class KotlinBasePlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.logger.lifecycle("Attempting to apply org.jetbrains.kotlin.jvm")
        if(!project.pluginManager.hasPlugin("org.jetbrains.kotlin.jvm")) {
            project.plugins.apply("org.jetbrains.kotlin.jvm")
            // project.plugins.apply("com.github.johnrengelman.shadow")
        }

        // project.tasks.withType(Jar::class.java).configureEach {
        //     if(!it.name.contains("shadowJar")) {
        //         it.dependsOn("shadowJar")
        //     }
        //
        // }

        // To eventually move into own meta plugin. Duplicated with the build.gradle.kts of this plugin folder
        project.tasks.withType(JavaCompile::class.java).configureEach {

            val projectName = project.name
            val taskName = it.name

            it.logger.lifecycle("Disabling java compile task for: task name: ${taskName}, project: ${projectName}")

            it.enabled = false
        }

        project.tasks.named("processResources") {
            val projectName = project.name
            val taskName = it.name

            it.logger.lifecycle("Disabling process resources task for: task name: ${taskName}, project: ${projectName}")

            it.enabled = false
        }

        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            project.logger.lifecycle("Plugin org.jetbrains.kotlin.jvm was just applied")
            project.dependencies.add(
                "implementation",
                project.dependencies.platform(project.dependencies.project(mapOf("path" to ":base:platforms:${"generalised-platform"}")))
            )
            project.dependencies.add(
                "testImplementation",
                project.dependencies.platform(project.dependencies.project(mapOf("path" to ":base:platforms:${"junit-platform"}")))
            )

        }
    }
}