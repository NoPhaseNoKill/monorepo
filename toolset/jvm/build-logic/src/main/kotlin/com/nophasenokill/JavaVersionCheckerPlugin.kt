package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project


class JavaVersionCheckerPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            plugins.apply("com.nophasenokill.java-version-checker-plugin")
        }
    }
}
