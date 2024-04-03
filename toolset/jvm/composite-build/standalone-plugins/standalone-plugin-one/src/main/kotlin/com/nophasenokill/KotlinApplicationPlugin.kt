package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaPlugin

class KotlinApplicationPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        project.pluginManager.apply("com.nophasenokill.kotlin-base-plugin")
        // project.pluginManager.apply("org.jetbrains.kotlin.jvm")


        /*
            This is reacting to the java plugin (created by org.jetbrains.kotlin.jvm) rather than eagerly applying
         */

        project.plugins.withType(JavaPlugin::class.java)  {

            project.pluginManager.apply("application")

            project.extensions.getByType(JavaApplication::class.java).mainClass.set("com.nophasenokill.App")

            project.dependencies.add("implementation", "org.slf4j:slf4j-api:2.0.12").apply {
                if(this is ExternalModuleDependency) {
                    val slf4jExclusion = mapOf(
                        "group" to "org.slf4j",
                        "module" to "slf4j-simple"
                    )

                    this.exclude(
                        slf4jExclusion
                    )
                }
            }

            project.dependencies.add("runtimeOnly", "org.slf4j:slf4j-simple:2.0.12")
            project.dependencies.add("testRuntimeOnly", "org.slf4j:slf4j-simple:2.0.12")
        }

        project.repositories.gradlePluginPortal()
    }
}