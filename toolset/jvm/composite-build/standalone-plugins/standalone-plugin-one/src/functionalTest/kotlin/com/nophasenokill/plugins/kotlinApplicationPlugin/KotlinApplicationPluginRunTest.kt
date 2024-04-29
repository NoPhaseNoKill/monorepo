package com.nophasenokill.plugins.kotlinApplicationPlugin


import com.nophasenokill.setup.runner.SharedRunnerDetails
import com.nophasenokill.setup.variations.FunctionalTest.addPluginsById
import com.nophasenokill.setup.variations.FunctionalTest.createGradleRunner
import com.nophasenokill.setup.variations.FunctionalTest.getAsyncResult
import com.nophasenokill.setup.variations.FunctionalTest.getTaskOutcome
import com.nophasenokill.setup.variations.FunctionalTest.launchAsyncWork
import com.nophasenokill.setup.variations.FunctionalTest.runExpectedSuccessTask
import com.nophasenokill.setup.variations.sharedRunnerDirLazy
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class KotlinApplicationPluginRunTest {


    @Test
    fun `should be able to run an application when the applications settings file includes the meta-plugins and generalised platform`() = runTest {

        val details = getAsyncResult {
            val runner = SharedRunnerDetails.SharedRunner.getRunner(sharedRunnerDirLazy().value)
            val details = createGradleRunner(runner)
            details
        }

        val settingsFile = details.settingsFile
        val buildFile = details.buildFile
        val projectDir = details.projectDir

        launchAsyncWork {
            settingsFile.writeText("""
            rootProject.name = "some-name"
            includeBuild("platforms")
            includeBuild("meta-plugins")
        """.trimIndent())
            addPluginsById(
                listOf(
                    "com.nophasenokill.kotlin-application-plugin"
                ),
                buildFile
            )
        }

        launchAsyncWork {
            createMetaPluginsIncludedBuild(projectDir)
        }

        launchAsyncWork {
            createPlatformsIncludedBuild(projectDir)
        }

        launchAsyncWork {
            val directoryPath = "src/main/kotlin/com/nophasenokill"
            val appDirectory = File(projectDir.path).resolve(directoryPath)
            appDirectory.mkdirs()

            val appFile  = File(appDirectory.path + "/App.kt")
            appFile.createNewFile()

            assert(2 + 2 == 4) { " 2 + 2 Should be 4" }

            appFile.writeText("""
            package com.nophasenokill;

            object App {
                /**
                 * Run the application.
                 *
                 * @param args command line arguments are ignored
                 */
                @JvmStatic
                fun main(args: Array<String>) {
                    val calculation = 2 + 2
                    assert(2 + 2 == 4) { " 2 + 2 Should be 4" }
                }
            }
        """.trimIndent())
        }


        launchAsyncWork {
            verifyRunTask(details.gradleRunner)
                    }
    }

    private suspend fun createMetaPluginsIncludedBuild(projectDir: File) = coroutineScope {
        val metaPluginsDir = File(projectDir.path).resolve("meta-plugins")
        metaPluginsDir.mkdirs()
        val metaPluginsSettingsFile = File(metaPluginsDir.path + "/settings.gradle.kts")

        metaPluginsSettingsFile.writeText("""
            rootProject.name = "meta-plugins"

            pluginManagement {
                repositories.gradlePluginPortal()
            }

            dependencyResolutionManagement {
                repositories.gradlePluginPortal()
            }

            include("meta-plugin-one")

            enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
            enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
        """.trimIndent())

        val metaPluginOneDir = File(projectDir.path).resolve("meta-plugins/meta-plugin-one")
        metaPluginOneDir.mkdirs()

        val metaPluginOneBuildFile = File(metaPluginOneDir.path + "/build.gradle.kts")
        metaPluginOneBuildFile.createNewFile()

        metaPluginOneBuildFile.writeText("""
            import org.gradle.api.tasks.testing.logging.TestLogEvent

            plugins {
                `java-gradle-plugin`
                id("org.jetbrains.kotlin.jvm") version "1.9.21"
            }


            group = "com.nophasenokill.meta-plugins"
            version = "0.1.local-dev"

            repositories {
                gradlePluginPortal()
            }

            gradlePlugin {
                val checkKotlinBuildServiceFixPlugin by plugins.creating {
                    id = "com.nophasenokill.meta-plugins.check-kotlin-build-service-fix-plugin"
                    implementationClass = "com.nophasenokill.CheckKotlinBuildServiceFixPlugin"
                }
            }

            /*
                 Fixes undeclared build service usage when using: enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
                 Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165

                 Note: Because of the structure of the whole root jvm project, this ALSO needs to be applied directly
                 to this level too, as well as exposing a re-usable plugin to fix the SAME issue for other included builds
                 or projects.
             */

            gradle.taskGraph.whenReady {
                val allTasks = gradle.taskGraph.allTasks
                allTasks.forEach {
                    gradle.sharedServices.registrations.all {
                        val buildServiceProvider = this.service
                        val buildService = buildServiceProvider.get()

                        val kotlinCollectorSearchString = "org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector"
                        val isCollectorService = buildService.toString().contains(kotlinCollectorSearchString)

                        if (isCollectorService) {
                            project.logger.debug(
                                "Applying checkKotlinGradlePluginConfigurationErrors workaround to task: {} for project: {}",
                                it,
                                project.name
                            )
                            it.usesService(buildServiceProvider)
                        }
                    }
                }
            }

            dependencies {

                implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.21"))
                implementation(platform("org.junit:junit-bom:5.10.1"))
                implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.8.0"))

                implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.21") {
                    exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core-jvm").because("It conflicts with coroutine BOM which expects 1.8.0 and this brings in 1.5.0")
                }

                implementation("org.jetbrains.kotlin:kotlin-stdlib")
                testImplementation("commons-io:commons-io:2.16.0")

                testImplementation(gradleTestKit())
            }

            testing {
                suites {

                    val test by getting(JvmTestSuite::class) {
                        useJUnitJupiter("5.10.1")

                        this.targets.configureEach {
                            this.testTask.configure {
                                this.testLogging {
                                    events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
                                    showStandardStreams = true
                                    minGranularity = 2
                                }
                            }
                        }
                    }

                    val functionalTest by registering(JvmTestSuite::class) {

                        this.targets.configureEach {
                            this.testTask.configure {
                                this.testLogging {
                                    events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
                                    showStandardStreams = true
                                    minGranularity = 2
                                }
                            }
                        }

                        useJUnitJupiter("5.10.1")

                        dependencies {
                            // functionalTest test suite depends on the production code in tests
                            implementation(project())
                            implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.21"))
                            implementation(platform("org.junit:junit-bom:5.10.1"))
                            implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.8.0")) {
                                exclude("org.jetbrains", "annotations")
                            }

                            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test") {
                                exclude("org.jetbrains", "annotations")
                            }
                            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm") {
                                exclude("org.jetbrains", "annotations")
                            }
                        }
                    }
                }
            }

            gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])


            val functionalTestTask = tasks.register("functionalTestTask") {


                inputs.files("src/functionalTest")
                inputs.files(tasks.jar)
                outputs.dirs(layout.buildDirectory.get().asFile.resolve("test-results/functionalTest"))
                outputs.dirs(layout.buildDirectory.get().asFile.resolve("classes/functionalTest"))
                outputs.dirs(layout.buildDirectory.get().asFile.resolve("kotlin/compileFunctionalTestKotlin"))

                dependsOn(tasks.jar)
                dependsOn(testing.suites.named("functionalTest"))
            }

            tasks.test {
                inputs.files("src/test")
                inputs.files(tasks.jar)
                outputs.dirs(layout.buildDirectory.get().asFile.resolve("test-results/test"))
                outputs.dirs(layout.buildDirectory.get().asFile.resolve("classes/test"))
                outputs.dirs(layout.buildDirectory.get().asFile.resolve("kotlin/compileTestKotlin"))

                dependsOn(tasks.jar)
            }

            tasks.named<Task>("check") {
                // Include functionalTest as part of the check which implicitly means build lifecycle
                dependsOn(functionalTestTask)
            }

        """.trimIndent())

        val pluginDirectoryPath = "src/main/kotlin/com/nophasenokill"
        val appDirectory = File(metaPluginOneDir.path).resolve(pluginDirectoryPath)
        appDirectory.mkdirs()

        val pluginFile  = File(appDirectory.path + "/CheckKotlinBuildServiceFixPlugin.kt")
        pluginFile.createNewFile()

        pluginFile.writeText("""
            package com.nophasenokill

            import org.gradle.api.Plugin
            import org.gradle.api.Project

            class CheckKotlinBuildServiceFixPlugin: Plugin<Project> {
                override fun apply(project: Project) {
                    /*
                         Fixes undeclared build service usage when using: enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
                         Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165
                     */
                    project.gradle.taskGraph.whenReady {
                        project.tasks.named("checkKotlinGradlePluginConfigurationErrors").configure { task ->
                            val kotlinCollectorSearchString = "org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector"
                            project.gradle.sharedServices.registrations.all { buildServiceRegistration ->
                                val buildServiceProvider = buildServiceRegistration.service
                                val buildService = buildServiceProvider.get()
                                val isCollectorService = buildService.toString().contains(kotlinCollectorSearchString)

                                if(isCollectorService) {
                                    project.logger.debug(
                                        "Applying checkKotlinGradlePluginConfigurationErrors workaround to task: {} for project: {}",
                                        task,
                                        project.name
                                    )
                                    task.usesService(buildServiceProvider)
                                }
                            }
                        }
                    }
                }
            }
        """.trimIndent())

    }

    private suspend fun createPlatformsIncludedBuild(projectDir: File)  = coroutineScope {
        val platformDir = File(projectDir.path).resolve("platforms")
        platformDir.mkdirs()
        val platformSettingsFile = File(platformDir.path + "/settings.gradle.kts")


        platformSettingsFile.writeText(
            """
                rootProject.name = "platforms"

                pluginManagement {
                    repositories.gradlePluginPortal()
                }

                dependencyResolutionManagement {
                    repositories.gradlePluginPortal()
                }

                include("generalised-platform")

                enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
                enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
            """.trimIndent()
        )

        val generalisedPlatformDir = File(projectDir.path).resolve("platforms/generalised-platform")
        generalisedPlatformDir.mkdirs()


        val generalisedPlatformBuildFile = File(generalisedPlatformDir.path + "/build.gradle.kts")
        generalisedPlatformBuildFile.createNewFile()
        generalisedPlatformBuildFile.writeText(
            """
                    plugins {
                        id("java-platform")
                    }

                    group = "com.nophasenokill.platforms"

                    dependencies {
                        constraints {
                            api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.21")
                            api("org.slf4j:slf4j-api:2.0.12")
                            api("org.slf4j:slf4j-simple:2.0.12")
                        }
                    }
            """.trimIndent()
        )
    }

    private suspend fun verifyRunTask(runner: GradleRunner) {
        launchAsyncWork {
            val result = runExpectedSuccessTask(runner, "run")
            Assertions.assertTrue(result.output.contains("BUILD SUCCESS"))
            val outcome = getTaskOutcome(":run", result)
            Assertions.assertEquals(outcome, TaskOutcome.SUCCESS)
        }

    }

}