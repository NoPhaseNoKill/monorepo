package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.internal.artifacts.DefaultModuleIdentifier
import org.gradle.api.internal.artifacts.dependencies.DefaultMinimalDependency
import org.gradle.api.internal.artifacts.dependencies.DefaultMutableVersionConstraint
import org.gradle.api.plugins.JavaApplication

class KotlinApplicationPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        project.pluginManager.apply("com.nophasenokill.meta-plugins.pin-kotlin-dependency-versions-plugin")
        project.pluginManager.apply("application")

        project.pluginManager.withPlugin("application") {
            project.extensions.getByType(JavaApplication::class.java).mainClass.set("com.nophasenokill.App")
        }

        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            project.pluginManager.apply("com.nophasenokill.meta-plugins.check-kotlin-build-service-fix-plugin")
            project.repositories.gradlePluginPortal()
        }
    }
}