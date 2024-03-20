package com.nophasenokill

import org.gradle.api.Project
import org.gradle.api.Plugin


class StandalonePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("greeting") {
            it.doLast {
                println("Hello from plugin 'standalone-plugin'")
            }
        }
    }
}
