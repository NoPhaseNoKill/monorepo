package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinBasePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if(!project.pluginManager.hasPlugin("org.jetbrains.kotlin.jvm")) {
            project.logger.lifecycle("Applying org.jetbrains.kotlin.jvm")
            project.plugins.apply("org.jetbrains.kotlin.jvm")
        }
    }
}