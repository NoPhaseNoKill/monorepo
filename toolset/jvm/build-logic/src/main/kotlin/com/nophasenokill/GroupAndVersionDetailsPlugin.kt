package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class GroupAndVersionDetailsPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            plugins.apply("com.nophasenokill.group-and-version-details-plugin")
        }
    }
}
