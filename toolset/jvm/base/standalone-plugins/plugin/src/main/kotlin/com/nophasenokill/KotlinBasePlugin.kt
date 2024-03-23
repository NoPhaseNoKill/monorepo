package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinBasePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.logger.lifecycle("Attempting to apply org.jetbrains.kotlin.jvm")
        if(!project.pluginManager.hasPlugin("org.jetbrains.kotlin.jvm")) {
            if(!project.pluginManager.hasPlugin("java")) {
                project.plugins.apply("java")
            }
            project.plugins.apply("org.jetbrains.kotlin.jvm")
        }
    }
}