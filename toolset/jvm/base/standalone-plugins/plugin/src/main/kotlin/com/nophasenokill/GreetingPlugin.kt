package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class GreetingPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        
        project.tasks.register("greeting") { task ->
            task.doLast {
                println("Hello from plugin 'com.nophasenokill.greeting'")
            }
        }
    }
}