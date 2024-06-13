package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.embeddedKotlinVersion

class ProducerPlugin: Plugin<Project> {
    override fun apply(target: Project) {

        target.run {


            pluginManager.withPlugin("java") {
                dependencies {
                    add("implementation", "org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:$embeddedKotlinVersion")
                    add("implementation", gradleApi())
                    add("testImplementation", gradleTestKit())
                    add("testImplementation", platform("org.junit:junit-bom:5.10.1"))
                    add("testImplementation", "org.junit.jupiter:junit-jupiter")
                    add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")

                    // Equivalent dependencies above
                    pluginManager.apply("com.nophasenokill.standalone-kotlin-base-plugin")
                }
            }

            logger.quiet("I am the producer plugin which is being applied to the ${project.name}")
        }
    }
}