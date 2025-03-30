package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project


class RepositoriesPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            plugins.apply("com.nophasenokill.repositories-plugin")
        }
    }
}
