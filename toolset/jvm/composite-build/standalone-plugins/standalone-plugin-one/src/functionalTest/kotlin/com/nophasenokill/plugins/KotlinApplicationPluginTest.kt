package com.nophasenokill.plugins

import com.nophasenokill.functionalTest.FunctionalTest
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files

class KotlinApplicationPluginTest: FunctionalTest() {

    @Test
    fun `should have only the configured dependencies`() {
        settingsFile.writeText("")
        addPluginsById(
            listOf(
                "com.nophasenokill.kotlin-application-plugin"
            ),
            buildFile
        )

        val result = runExpectedSuccessTask("dependencies")

        val file = getResourceFile("dependencies/kotlin-application-expected-dependencies.txt")
        val expectedContent = file.readText()

        /*
            The contents of this file don't include shifting behaviour.

            Meaning: These lines should never change unless a configuration is updated or modified in a way
            that affects dependencies
         */

        val comparableLines = getComparableBuildResultLines(result, 5, 4)

        Assertions.assertLinesMatch(expectedContent.lines(), comparableLines)
    }

    @Test
    fun `should be able to run an application`() {
        settingsFile.writeText("")
        addPluginsById(
            listOf(
                "com.nophasenokill.kotlin-application-plugin"
            ),
            buildFile
        )

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