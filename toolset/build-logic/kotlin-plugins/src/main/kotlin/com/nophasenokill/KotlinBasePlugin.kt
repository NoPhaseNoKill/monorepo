package com.nophasenokill

import com.nophasenokill.extensions.configureTask
import com.nophasenokill.extensions.configureTasks
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemon
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerExecutionStrategy

class KotlinBasePlugin: Plugin<Project> {
    override fun apply(project: Project) {

        project.run {

            group = "com.nophasenokill"
            version = "0.1.local-dev"

            plugins.apply("org.jetbrains.kotlin.jvm")
            plugins.apply("com.nophasenokill.task-events-plugin")
            plugins.apply("com.nophasenokill.hashing-tasks-plugin")
            plugins.apply("com.nophasenokill.java-version-checker-plugin")
            plugins.apply("com.nophasenokill.test-report-data-consumer-plugin")
            plugins.apply("com.nophasenokill.test-report-data-producer-plugin")
            plugins.apply("com.nophasenokill.incremental-test-plugin")

            configureTask<DefaultTask>("check") {
                dependsOn("checkJavaVersion")
                dependsOn("incrementalTest")
            }

            configureTask<DefaultTask>("build") {
                dependsOn("hashKotlinSourceSet")
            }


            val kotlinJvmProjectExtension = extensions.findByType(KotlinJvmProjectExtension::class.java)


            kotlinJvmProjectExtension?.run {

                kotlinDaemonJvmArgs = listOf(
                    "-Xmx2g",
                    "-XX:MaxMetaspaceSize=384m",
                    "-Dfile.encoding=UTF-8",
                    "-XX:+HeapDumpOnOutOfMemoryError"
                )
            }

            val versionCatalog = project.extensions.findByType(VersionCatalogsExtension::class.java)?.named("libs")
            val kotlinVersion = versionCatalog?.findVersion("kotlin")?.get().toString()

            configureTasks<KotlinCompilationTask<*>> {
                println("Setting kotlin language version to: ${KotlinVersion.DEFAULT}")
                compilerOptions {
                    languageVersion.set(KotlinVersion.DEFAULT)
                    progressiveMode.set(true)
                }
            }

            configureTasks<KotlinCompile> {
                jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.ERROR)
                useDaemonFallbackStrategy.set(false)
            }

            configureTasks<CompileUsingKotlinDaemon> {
                compilerExecutionStrategy.set(KotlinCompilerExecutionStrategy.DAEMON)
            }

            /*
                See: https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
             */
            configureTasks<AbstractArchiveTask> {
                isPreserveFileTimestamps = false
                isReproducibleFileOrder = true
            }

            /*
                See: https://docs.gradle.org/current/userguide/common_caching_problems.html#suggestions_for_authoring_your_build
             */
            configureTask<Jar>("jar") {
                manifest {
                    val classPath = configurations.named("runtimeClasspath").get().map { it.name }.joinToString(" ")
                    attributes("Class-Path" to classPath)
                }
            }


            val junitVersion = versionCatalog?.findVersion("junit")?.get().toString()
            val junitPlatformVersion = versionCatalog?.findVersion("junitPlatform")?.get().toString()

            val coroutinesVersion = versionCatalog?.findVersion("coroutines")?.get().toString()

            dependencies {
                constraints.add("implementation", "org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}")
                constraints.add("implementation", "org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")

                add("implementation", gradleApi())
                /*
                    Equivalent of: implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${libs.versions.kotlin.get()}")
                 */
                add("implementation", "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

                add("implementation", "org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}")
                add("implementation", "org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")

                add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${coroutinesVersion}")


                add("testImplementation", "org.junit.jupiter:junit-jupiter-api:$junitVersion")
                add("testImplementation", "org.junit.jupiter:junit-jupiter:$junitVersion")
                add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher:$junitPlatformVersion")

            }

            configureTasks<Test> {
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
                testLogging.showStackTraces = true
                testLogging.exceptionFormat = TestExceptionFormat.FULL
                testLogging.showStandardStreams
                testLogging.minGranularity = 2
            }
        }
    }
}
