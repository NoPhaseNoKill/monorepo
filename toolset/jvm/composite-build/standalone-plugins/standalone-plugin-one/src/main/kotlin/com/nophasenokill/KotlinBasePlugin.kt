package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension


class KotlinBasePlugin : Plugin<Project> {
    override fun apply(project: Project) {

        if(!project.pluginManager.hasPlugin("com.nophasenokill.java-base-plugin")) {
            project.pluginManager.apply("com.nophasenokill.java-base-plugin")
        }

        project.tasks.register("greeting") {
            it.doLast {
                println("Hello from plugin 'com.nophasenokill.kotlin-base-plugin'")
            }
        }

        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {

            project.dependencies.add("implementation", project.dependencies.platform("org.jetbrains.kotlin:kotlin-bom:1.9.21"))
            /*
                Adds gradle api, as well as the test kit.

                Useful for things like the slf4j logger
             */
            project.dependencies.add("testImplementation", project.dependencies.gradleTestKit())

            project.kotlinExtension.jvmToolchain(21)
        }
    }
}