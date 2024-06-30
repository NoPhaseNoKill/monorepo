package com.nophasenokill.plugins.checkKotlinBuildServiceFixPlugin

import com.nophasenokill.setup.junit.extensions.SharedTestSuiteStore
import com.nophasenokill.setup.variations.FunctionalTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import java.io.File


class CheckKotlinBuildServiceFixPluginFunctionalTest: FunctionalTest() {

    @Test
    fun `should fine build service warning without plugin, and should not receive build service warning when plugin is applied` (context: ExtensionContext) = runTest {

        val details = SharedTestSuiteStore.getSharedGradleRunnerDetails(context)
        val settingsFile = details.settingsFile
        val buildFile = details.buildFile
        val projectDir = details.projectDir

        val someProjectName = "some-project"
        val someProjectSubProjectName = "${someProjectName}-sub-project-one"

        val someProject2Name = "some-project2"
        val someProject2SubProjectName = "${someProject2Name}-sub-project-one"

        val someProject = createIncludedBuildWithSubproject(someProjectName, projectDir, someProjectSubProjectName)
        val someProject2 = createIncludedBuildWithSubproject(someProject2Name, projectDir, someProject2SubProjectName)


        writeText(buildFile) {
            """
            tasks.register("buildAllComposite") {
                group = "verification"
                description = "Builds all projects, which includes assembling them and running all checks (tests/functional tests)"

                dependsOn(gradle.includedBuild("$someProjectName").task(":${someProjectSubProjectName}:build"))
                dependsOn(gradle.includedBuild("$someProject2Name").task(":${someProject2SubProjectName}:build"))
            }
        """.trimIndent()
        }

        writeText(settingsFile) {
            """
            rootProject.name = "some-name"
            includeBuild("$someProjectName")
            includeBuild("$someProject2Name")

            pluginManagement {
                repositories.gradlePluginPortal()
            }

            dependencyResolutionManagement {
                repositories.gradlePluginPortal()
            }
        """.trimIndent()
        }

        // Assert they exist without plugin
        val result = runExpectedSuccessTask(details, "buildComposite")
        val warningsLines = readLines(result).filter { it.contains("Build service 'KotlinToolingDiagnosticsCollector") }

        Assertions.assertTrue(result.output.contains("BUILD SUCCESS"))
        Assertions.assertEquals(2, warningsLines.size)

        // Assert they don't exist with plugin
        writeText(someProject.subProjects.first().buildFile) {
            """
            plugins {
                kotlin("jvm") version("1.9.21")
                id("com.nophasenokill.meta-plugins.check-kotlin-build-service-fix-plugin")
            }
        """.trimIndent()
        }

        writeText(someProject2.subProjects.first().buildFile) {
            """
            plugins {
                kotlin("jvm") version("1.9.21")
                id("com.nophasenokill.meta-plugins.check-kotlin-build-service-fix-plugin")
            }
        """.trimIndent()
        }

        val resultWithPlugin = runExpectedSuccessTask(details, "buildAllComposite")
        val warningLinesWithPlugin = readLines(resultWithPlugin).filter { it.contains("Build service 'KotlinToolingDiagnosticsCollector") }

        Assertions.assertTrue(resultWithPlugin.output.contains("BUILD SUCCESS"))
        Assertions.assertEquals(0, warningLinesWithPlugin.size)
    }

    private data class InitialIncludedBuild(
        val directory: File,
        val buildFile: File,
        val settingsFile: File,
    )

    private data class BasicIncludedBuild(
        val includedBuild: InitialIncludedBuild,
        val subProjects: List<BasicSubProject>
    )

    private data class BasicSubProject(
        val directory: File,
        val buildFile: File,
    )


    private suspend fun createIncludedBuildWithSubproject(
        includedBuildName: String,
        projectDir: File,
        subProjectName: String
    ): BasicIncludedBuild {

        val includedBuild = createBasicIncludedBuild(includedBuildName, projectDir)
        appendText(includedBuild.settingsFile) {
            """
            include("$subProjectName")

            pluginManagement {
                repositories.gradlePluginPortal()
            }

            dependencyResolutionManagement {
                repositories.gradlePluginPortal()
            }
        """.trimIndent()
        }
        val subProjectDir = includedBuild.directory.resolve(subProjectName)
        subProjectDir.mkdirs()
        val subProjectBuildFile = File(subProjectDir.path + "/build.gradle.kts")
        createFile(subProjectBuildFile)

        writeText(subProjectBuildFile) {
            """
            plugins {
                kotlin("jvm") version("1.9.21")
            }
        """.trimIndent()
        }

        val subProject = BasicSubProject(subProjectDir, subProjectBuildFile)

        return BasicIncludedBuild(includedBuild, listOf(subProject))

    }

    private fun createBasicIncludedBuild(includedBuildName: String, projectDir: File): InitialIncludedBuild {
        val dir = File(projectDir.path).resolve(includedBuildName)
        dir.mkdirs()

        val gradleDir = File(projectDir.path).resolve("gradle")
        gradleDir.mkdirs()
        val libs = File(gradleDir.path + "/libs.versions.toml")
        createFile(libs)
        writeText(libs) {
            """
            [versions]
            ## BOM's/frequently updated
            kotlin = "1.9.23"
            coroutines = "1.8.0"
            slf4j = "2.0.12"
            gradle = "8.7"
            junit = "5.10.1"
            commonsIo = "2.16.0"

            [plugins]
            kotlinJvm = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }
            #kotlinDsl = { id = "org.gradle.kotlin.kotlin-dsl", version.ref = "kotlinDsl" }
        """.trimIndent()
        }

        val settingsFile = File(dir.path + "/settings.gradle.kts")
        val buildFile = File(dir.path + "/build.gradle.kts")
        createFile(buildFile)

        writeText(settingsFile) {
            """
            rootProject.name = "$includedBuildName"

            enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
            enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

        """.trimIndent()
        }

        return InitialIncludedBuild(dir, buildFile, settingsFile)
    }
}