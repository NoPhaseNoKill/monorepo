package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class BasicPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        if(!project.plugins.hasPlugin("org.gradle.kotlin.kotlin-dsl")) {
            project.apply{
                plugin("org.gradle.kotlin.kotlin-dsl")
            }
        }



        project.pluginManager.withPlugin("org.gradle.kotlin.kotlin-dsl") {
            project.logger.quiet("Kotlin DSL was applied. It may have happened before this")



            val implementation = project.configurations.findByName("implementation")
            val runtimeOnly = project.configurations.findByName("runtimeOnly")

            val testImplementation = project.configurations.findByName("testImplementation")
            val testRuntimeOnly = project.configurations.findByName("testRuntimeOnly")
            val apiElements = project.configurations.findByName("apiElements")
            val runtimeClasspath = project.configurations.findByName("apiElements")

            val pluginImplementation = project.configurations.create("pluginImplementation")
            val pluginRuntimeOnly = project.configurations.create("pluginRuntimeOnly")

            val pluginTestImplementation = project.configurations.create("pluginTestImplementation")
            val pluginTestRuntimeOnly = project.configurations.create("pluginTestRuntimeOnly")

            val pluginApiElements = project.configurations.create("pluginApiElements")
            val pluginRuntimeClasspath = project.configurations.create("pluginRuntimeClasspath")

            pluginImplementation.extendsFrom(implementation)
            pluginRuntimeOnly.extendsFrom(runtimeOnly)
            pluginTestImplementation.extendsFrom(testImplementation)
            pluginTestRuntimeOnly.extendsFrom(testRuntimeOnly)
            pluginApiElements.extendsFrom(apiElements)
            pluginRuntimeClasspath.extendsFrom(runtimeClasspath)

            // configuring existing configurations not to put transitive dependencies on the compile classpath
            // this way you can avoid issues with implicit dependencies to transitive libraries
            project.configurations.findByName("compileClasspath")?.isTransitive = false
            project.configurations.findByName("testCompileClasspath")?.isTransitive = false

        }

        project.dependencies.apply {
            gradleApi()
            gradleTestKit()
            // add("testImplementation", "org.junit.jupiter:junit-jupiter:5.10.1")
            // add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
        }
    }
}