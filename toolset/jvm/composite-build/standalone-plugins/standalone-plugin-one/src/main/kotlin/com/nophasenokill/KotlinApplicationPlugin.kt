package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.bundling.Tar
import org.gradle.api.tasks.bundling.Zip

class KotlinApplicationPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        project.pluginManager.apply("org.jetbrains.kotlin.jvm")

        project.pluginManager.apply("application")


        /*
            This is reacting to the java plugin rather than eagerly applying
         */

        project.plugins.withType(JavaPlugin::class.java) {
            project.extensions.getByType(JavaApplication::class.java).mainClass.set("com.nophasenokill.App")
        }


        project.dependencies.add("implementation", "org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.22")

        project.repositories.gradlePluginPortal()

        project.tasks.withType(Tar::class.java).configureEach {
            it.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }

        project.tasks.withType(Zip::class.java).configureEach {
            it.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    }
}