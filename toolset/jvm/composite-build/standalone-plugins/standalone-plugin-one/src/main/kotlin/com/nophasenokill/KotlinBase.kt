package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class KotlinBase: Plugin<Project> {
    override fun apply(project: Project) {

        project.pluginManager.apply("com.nophasenokill.meta-plugins.pin-kotlin-dependency-versions-plugin")

        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            project.pluginManager.apply("com.nophasenokill.meta-plugins.check-kotlin-build-service-fix-plugin")
            project.repositories.gradlePluginPortal()
        }
    }
}