package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinLibraryPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            plugins.apply("com.nophasenokill.kotlin-base-plugin")
            plugins.apply("java-library")
            plugins.apply("com.nophasenokill.jacoco-plugin")
            plugins.apply("com.nophasenokill.idea-sources-download-plugin")
        }
    }
}
