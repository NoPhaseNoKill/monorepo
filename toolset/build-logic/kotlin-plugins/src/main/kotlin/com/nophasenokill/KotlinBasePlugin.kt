package com.nophasenokill

import com.nophasenokill.extensions.configureTask
import com.nophasenokill.extensions.named
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.DocsType
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemon
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerExecutionStrategy
import org.jetbrains.kotlin.gradle.utils.named

class KotlinBasePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            group = "com.nophasenokill"
            version = "0.1.local-dev"

            plugins.apply("org.jetbrains.kotlin.jvm")

            val kotlinJvmProjectExtension = extensions.findByType(KotlinJvmProjectExtension::class.java)

            kotlinJvmProjectExtension?.run {

                jvmToolchain {
                    compilerOptions.languageVersion.set(KotlinVersion.KOTLIN_2_0)
                }

                kotlinDaemonJvmArgs = listOf(
                    "-Xmx2g",
                    "-XX:MaxMetaspaceSize=384m",
                    "-Dfile.encoding=UTF-8",
                    "-XX:+HeapDumpOnOutOfMemoryError"
                )
            }

            tasks.withType(KotlinCompile::class.java).configureEach {
                it.jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.ERROR)
            }

            tasks.withType(CompileUsingKotlinDaemon::class.java).configureEach {
                it.compilerExecutionStrategy.set(KotlinCompilerExecutionStrategy.DAEMON)
            }

            tasks.withType(KotlinCompile::class.java).configureEach {
                it.useDaemonFallbackStrategy.set(false)
            }

            val versionCatalog = project.extensions.findByType(VersionCatalogsExtension::class.java)?.named("libs")
            val junitVersion = versionCatalog?.findVersion("junit")?.get().toString()
            val junitPlatformVersion = versionCatalog?.findVersion("junitPlatform")?.get().toString()
            val kotlinVersion = versionCatalog?.findVersion("kotlin")?.get().toString()
            val coroutinesVersion = versionCatalog?.findVersion("coroutines")?.get().toString()

            dependencies.run {

                constraints.add("implementation", "org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}")
                constraints.add("implementation", "org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")

                add("implementation", gradleApi())
                /*
                    Equivalent of: implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${libs.versions.kotlin.get()}")
                 */
                add("implementation", "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

                add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${coroutinesVersion}")


                add("testImplementation", "org.junit.jupiter:junit-jupiter-api:$junitVersion")
                add("testImplementation", "org.junit.jupiter:junit-jupiter:$junitVersion")
                add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
            }

            tasks.withType(org.gradle.api.tasks.testing.Test::class.java).configureEach { test ->
                test.useJUnitPlatform()
                test.testLogging.events = setOf(
                    TestLogEvent.STANDARD_OUT,
                    TestLogEvent.STARTED,
                    TestLogEvent.PASSED,
                    TestLogEvent.SKIPPED,
                    TestLogEvent.FAILED,
                    TestLogEvent.STANDARD_OUT,
                    TestLogEvent.STANDARD_ERROR,
                )

                test.testLogging.minGranularity = 2
            }

            configureTestReport()
        }
    }

    private fun Project.configureTestReport() {
        // Disable the test report for the individual test task
        configureTask<Test>("test") {
            reports.html.required.set(false)
        }

        // Share the test report data to be aggregated for the whole project
        configurations.create("binaryTestResultsElements") {
            it.isCanBeResolved = false
            it.isCanBeConsumed = true

            it.attributes { attribute ->
                attribute.attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
                attribute.attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("test-report-data"))
            }

            val testTask = tasks.named<AbstractTestTask>("test")

            it.outgoing.artifact(testTask.map { task -> task.binaryResultsDirectory.get() })
        }
    }
}