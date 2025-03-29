package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project


class PluginBase: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            plugins.apply("com.nophasenokill.plugin-base")
        }
    }
}
