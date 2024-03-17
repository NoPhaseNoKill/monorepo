package com.nophasenokill

import org.gradle.api.Project
import org.gradle.api.Plugin


class StandalonePluginPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("greeting") {
            this.doLast {
                println("Hello from plugin 'com.nophasenokill.plugins-new.plugin'")
            }
        }
    }
}
