package com.nophasenokill

import org.gradle.api.Project
import org.gradle.api.Plugin

/**
 * A simple 'hello world' plugin.
 */
class PluginTestedOne: Plugin<Project> {
    override fun apply(project: Project) {
        // Register a task
        project.tasks.register("someTask") { task ->
            task.doLast {
                println("Hello from plugin 'com.nophasenokill.someTask'")
            }
        }
    }
}
