package com.nophasenokill

import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class BasicPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        if(!project.plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
            project.apply{
                plugin("org.jetbrains.kotlin.jvm")
            }
        }

        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            project.logger.quiet("Kotlin jvm plugin was applied. It may have happened before this")


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

        project.run {

            val versionCatalog = extensions.getByType(VersionCatalogsExtension::class.java)
            val junitVersion = requireNotNull(versionCatalog.named("libs").findVersion("junit").get()) {
                "Version catalog name junit required for basic plugin"
            }
            val junitPlatformVersion = requireNotNull(versionCatalog.named("libs").findVersion("junitPlatform").get()) {
                "Version catalog name junitPlatform required for basic plugin"
            }
            dependencies {
                add("testImplementation", "org.junit.jupiter:junit-jupiter-api:${junitVersion}")

                /*
                    To not implicitly load test framework. https://docs.gradle.org/8.7/userguide/upgrading_version_8.html#test_framework_implementation_dependencies
                 */
                add("testImplementation", "org.junit.jupiter:junit-jupiter:${junitVersion}")
                add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher:${junitPlatformVersion}")
            }

            val testTasks = tasks.withType(Test::class.java)

            testTasks.configureEach {
                useJUnitPlatform()

                testLogging.events = setOf(
                    TestLogEvent.STANDARD_OUT,
                    TestLogEvent.STARTED,
                    TestLogEvent.PASSED,
                    TestLogEvent.SKIPPED,
                    TestLogEvent.FAILED,
                    TestLogEvent.STANDARD_OUT,
                    TestLogEvent.STANDARD_ERROR,
                )
            }
        }
    }
}