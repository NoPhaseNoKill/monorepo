package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinLibraryPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            plugins.apply("java-library")
            plugins.apply("com.nophasenokill.kotlin-base-plugin")

            plugins.apply("com.nophasenokill.idea-sources-download-plugin")
        }
    }
}
