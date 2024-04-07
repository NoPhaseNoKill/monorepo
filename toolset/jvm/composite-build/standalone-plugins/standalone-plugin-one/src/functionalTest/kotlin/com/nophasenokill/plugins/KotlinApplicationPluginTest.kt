package com.nophasenokill.plugins

import com.nophasenokill.functionalTest.FunctionalTest
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files

class KotlinApplicationPluginTest: FunctionalTest() {

    @Test
    fun `should have only the configured dependencies`() {
        settingsFile.writeText("""
            rootProject.name = "some-name"
            includeBuild("platforms")
        """.trimIndent())

        addPluginsById(
            listOf(
                "com.nophasenokill.kotlin-application-plugin"
            ),
            buildFile
        )

        val platformDir = File(projectDir.path).resolve("platforms")
        platformDir.mkdirs()
        val platformSettingsFile  = File(platformDir.path + "/settings.gradle.kts")


        platformSettingsFile.writeText("""
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
        """.trimIndent())

        val generalisedPlatformDir = File(projectDir.path).resolve("platforms/generalised-platform")
        generalisedPlatformDir.mkdirs()

        val generalisedPlatformBuildFile  = File(generalisedPlatformDir.path + "/build.gradle.kts")
        generalisedPlatformBuildFile.createNewFile()
        generalisedPlatformBuildFile.writeText("""
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
        """.trimIndent())

        val dependenciesResult = runExpectedSuccessTask("dependencies")
        val dependenciesOutcome = getTaskOutcome(":dependencies", dependenciesResult)

        /*
            The contents of this file don't include shifting behaviour.

            Meaning: These lines should never change unless a configuration is updated or modified in a way
            that affects dependencies

            Note: These are effectively the same thing - it's just now picking the version from the constraint.

                Using no platform example:

                    \--- org.slf4j:slf4j-api:2.0.12

                Using platform example:

                    +--- com.nophasenokill.platforms:generalised-platform -> project :platforms:generalised-platform
                    |    \--- org.slf4j:slf4j-api:2.0.12 (c)
                    \--- org.slf4j:slf4j-api -> 2.0.12


            To verify this: You can run ./gradlew :applications:application-one:dependencyInsight --configuration testCompileClasspath --dependency slf4j --scan

                > Task :applications:application-one:dependencyInsight
                org.slf4j:slf4j-api:2.0.12 (by constraint) <------------------------****THIS IS THE IMPORTANT PART****
                  Variant compile:
                    | Attribute Name                     | Provided | Requested    |
                    |------------------------------------|----------|--------------|
                    | org.gradle.status                  | release  |              |
                    | org.gradle.category                | library  | library      |
                    | org.gradle.libraryelements         | jar      | classes      |
                    | org.gradle.usage                   | java-api | java-api     |
                    | org.gradle.dependency.bundling     |          | external     |
                    | org.gradle.jvm.environment         |          | standard-jvm |
                    | org.gradle.jvm.version             |          | 21           |
                    | org.jetbrains.kotlin.platform.type |          | jvm          |


         */
        val file = getResourceFile("dependencies/kotlin-application-expected-dependencies.txt")
        val expectedContent = file.readText()
        val comparableLines = getComparableBuildResultLines(dependenciesResult, 9, 10)

        Assertions.assertLinesMatch(expectedContent.lines(), comparableLines)
        Assertions.assertEquals(dependenciesOutcome, TaskOutcome.SUCCESS)
    }

    @Test
    fun `should be able to run an application when the applications settings file includes the generalised platform`() {
        settingsFile.writeText("""
            rootProject.name = "some-name"
            includeBuild("platforms")
        """.trimIndent())
        addPluginsById(
            listOf(
                "com.nophasenokill.kotlin-application-plugin"
            ),
            buildFile
        )

        val platformDir = File(projectDir.path).resolve("platforms")
        platformDir.mkdirs()
        val platformSettingsFile  = File(platformDir.path + "/settings.gradle.kts")


        platformSettingsFile.writeText("""
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
        """.trimIndent())

        val generalisedPlatformDir = File(projectDir.path).resolve("platforms/generalised-platform")
        generalisedPlatformDir.mkdirs()


        val generalisedPlatformBuildFile  = File(generalisedPlatformDir.path + "/build.gradle.kts")
        generalisedPlatformBuildFile.createNewFile()
        generalisedPlatformBuildFile.writeText("""
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
        """.trimIndent())


        val directoryPath = "src/main/kotlin/com/nophasenokill"
        val appDirectory = File(projectDir.path).resolve(directoryPath)
        appDirectory.mkdirs()

        val appFile  = File(appDirectory.path + "/App.kt")
        appFile.createNewFile()

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
                    print("Calculation was: ")
                    print(calculation)
                }
            }
        """.trimIndent())

        val result = runExpectedSuccessTask("run")
        val outcome = getTaskOutcome(":run", result)

        Assertions.assertTrue(result.output.contains("BUILD SUCCESS"))
        Assertions.assertTrue(result.output.contains("Calculation was: 4"))
        Assertions.assertEquals(outcome, TaskOutcome.SUCCESS)
    }

}