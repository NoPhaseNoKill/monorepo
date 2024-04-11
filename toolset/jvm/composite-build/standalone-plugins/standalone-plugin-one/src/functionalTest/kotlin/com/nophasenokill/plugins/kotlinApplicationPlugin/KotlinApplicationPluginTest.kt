package com.nophasenokill.plugins.kotlinApplicationPlugin

import com.nophasenokill.setup.runner.SharedRunnerDetails
import com.nophasenokill.setup.variations.FunctionalTest
import kotlinx.coroutines.test.runTest
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import java.io.File

class KotlinApplicationPluginTest: FunctionalTest() {

    @Test
    fun `should be able to run an application, and maintain same dependencies, when the applications settings file includes the generalised platform`(context: ExtensionContext) = runTest {
        val details = createGradleRunner(context)
        val settingsFile = details.settingsFile
        val buildFile = details.buildFile
        val projectDir = details.projectDir

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

        verifyRunTask(details)
        verifyDependencies(details)
    }

    private fun verifyRunTask(details: SharedRunnerDetails) {
        val runResult = runExpectedSuccessTask(details, "run")
        val runOutcome = getTaskOutcome(":run", runResult)

        Assertions.assertTrue(runResult.output.contains("BUILD SUCCESS"))
        Assertions.assertTrue(runResult.output.contains("Calculation was: 4"))
        Assertions.assertEquals(runOutcome, TaskOutcome.SUCCESS)
    }

    private fun verifyDependencies(details: SharedRunnerDetails) {
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

        val dependenciesResult = runExpectedSuccessTask(details, "dependencies")
        val dependenciesOutcome = getTaskOutcome(":dependencies", dependenciesResult)

        val file = getResourceFile("dependencies/kotlin-application-expected-dependencies.txt")
        val expectedContent = file.readText()
        val comparableLines = getComparableBuildResultLines(dependenciesResult, 9, 10)

        Assertions.assertLinesMatch(expectedContent.lines(), comparableLines)
        Assertions.assertEquals(dependenciesOutcome, TaskOutcome.SUCCESS)
    }

}