package com.nophasenokill

import com.nophasenokill.extensions.configureTask
import com.nophasenokill.extensions.configureTasks
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.DocsType
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemon
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerExecutionStrategy

class KotlinBasePlugin: Plugin<Project> {
    override fun apply(project: Project) {

        project.run {

            group = "com.nophasenokill"
            version = "0.1.local-dev"

            plugins.apply("org.jetbrains.kotlin.jvm")
            plugins.apply("com.nophasenokill.task-events-plugin")

            plugins.apply("com.nophasenokill.java-version-checker-plugin")

            tasks.named("check").configure {
                dependsOn("checkJavaVersion")
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

            configureTasks<KotlinCompile> {
                jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.ERROR)
                useDaemonFallbackStrategy.set(false)
            }

            configureTasks<CompileUsingKotlinDaemon> {
                compilerExecutionStrategy.set(KotlinCompilerExecutionStrategy.DAEMON)
            }


            val versionCatalog = project.extensions.findByType(VersionCatalogsExtension::class.java)?.named("libs")
            val junitVersion = versionCatalog?.findVersion("junit")?.get().toString()
            val junitPlatformVersion = versionCatalog?.findVersion("junitPlatform")?.get().toString()
            val kotlinVersion = versionCatalog?.findVersion("kotlin")?.get().toString()
            val coroutinesVersion = versionCatalog?.findVersion("coroutines")?.get().toString()

            dependencies {
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

                testLogging.minGranularity = 2
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
            isCanBeResolved = false
            isCanBeConsumed = true

            attributes {
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
                attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("test-report-data"))
            }

            val testTask = project.tasks.named<AbstractTestTask>("test")

            outgoing.artifact(testTask.map { task -> task.binaryResultsDirectory.get() })
        }
    }
}
