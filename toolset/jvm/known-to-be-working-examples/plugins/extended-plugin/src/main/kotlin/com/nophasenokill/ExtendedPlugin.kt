package com.nophasenokill

import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlatformExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType

class ExtendedPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        /*
            This has transitives of:
                - domain:person (relative to root)
                - domain:account (relative to root)
         */
        project.apply {
            plugin("org.gradle.kotlin.kotlin-dsl")
            plugin("org.gradle.java-gradle-plugin")
            plugin("com.nophasenokill.hash-source-plugin")
        }

        project.pluginManager.withPlugin("java") {
            val javaExtension = project.extensions.getByType<JavaPluginExtension>()

            javaExtension.toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
        }

        project.tasks.register("checkJavaVersion") {
            doLast {
                if(JavaVersion.current() != JavaVersion.VERSION_21) {
                    throw GradleException("This build must be run with java 21")
                }
            }
        }
    }
}