package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ProducerPlugin: Plugin<Project> {
    override fun apply(target: Project) {

        target.run {
            pluginManager.withPlugin("java") {
                dependencies {
                    add("implementation", dependencies.platform("org.jetbrains.kotlin:kotlin-bom:1.9.23"))
                    add("implementation", "org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
                    add("testImplementation", dependencies.platform("org.junit:junit-bom:5.10.1"))
                    add("testImplementation", "org.junit.jupiter:junit-jupiter")
                    add("testRuntimeOnly", dependencies.platform("org.jetbrains.kotlin:kotlin-bom:1.9.23"))
                    add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
                }
            }

            logger.quiet("I am the producer plugin which is being applied to the ${project.name}")
        }
    }
}