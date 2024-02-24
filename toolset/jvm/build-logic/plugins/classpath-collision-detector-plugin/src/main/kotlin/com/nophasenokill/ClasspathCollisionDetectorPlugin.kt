package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME
import org.gradle.kotlin.dsl.assign
import org.gradle.language.base.plugins.LifecycleBasePlugin.VERIFICATION_GROUP

abstract class ClasspathCollisionDetectorPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val task = project.tasks.register(TASK_NAME, ClasspathCollisionDetectorTask::class.java) {
            this.description = "Detect potential classpath collisions between library jars."
            this.group = VERIFICATION_GROUP
            this.collisionExclusions.set(DEFAULT_EXCLUSIONS)
            this.outputDirectory = project.layout.buildDirectory.dir("detectClasspathCollisionsTask")
        }

        project.pluginManager.withPlugin("java") {
            val runtimeClasspath = project.configurations.getByName(RUNTIME_CLASSPATH_CONFIGURATION_NAME)
            task.configure {
                this.configurations.from(runtimeClasspath)
            }
        }
    }

    companion object {
        private val DEFAULT_EXCLUSIONS: List<String> = listOf("META-INF/**", "module-info.class", "**.properties")

        private const val TASK_NAME: String = "detectClasspathCollisions"
    }
}