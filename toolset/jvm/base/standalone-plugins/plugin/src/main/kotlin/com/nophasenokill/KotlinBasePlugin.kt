package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.language.jvm.tasks.ProcessResources

class KotlinBasePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.logger.lifecycle("Attempting to apply org.jetbrains.kotlin.jvm")
        if(!project.pluginManager.hasPlugin("org.jetbrains.kotlin.jvm")) {
            project.plugins.apply("org.jetbrains.kotlin.jvm")

            project.tasks.withType(JavaCompile::class.java).configureEach {
                it.enabled = false
            }

            project.tasks.withType(ProcessResources::class.java).configureEach {
                val projectName = project.name
                val taskName = it.name
                val logger = project.logger

                project.gradle.taskGraph.whenReady {
                    if(taskName === "processResources") {
                        logger.lifecycle("Disabling process resources task for: task name: ${taskName}, project: ${projectName}")
                    }
                }

                if(taskName === "processResources") {
                    it.enabled = false
                }
            }
        }
    }
}