package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion

class JavaBasePlugin : Plugin<Project> {
    override fun apply(project: Project) {

        if (!project.pluginManager.hasPlugin("java")) {
            project.pluginManager.apply("java")
        }

        project.pluginManager.withPlugin("java") {

            project.repositories.gradlePluginPortal()

            val javaPluginExtension = project.extensions.getByType(JavaPluginExtension::class.java)

            javaPluginExtension.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

            project.dependencies.add("testImplementation", project.dependencies.platform("org.junit:junit-bom:5.10.1"))
            project.dependencies.add("testImplementation", "org.junit.jupiter:junit-jupiter")
            project.dependencies.add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")

        }
    }
}