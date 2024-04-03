package com.nophasenokill.plugins

import com.nophasenokill.functionalTest.FunctionalTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StandalonePluginOneTest: FunctionalTest() {

    @Test
    fun `should have only the configured dependencies`() {
        settingsFile.writeText("rootProject.name = \"standalone-plugins\"")

        val buildScriptFile = getResourceFile("build-scripts/standalone-plugin-one.txt")
        val standalonePluginOneBuildScript = buildScriptFile.readText()

        buildFile.writeText(standalonePluginOneBuildScript)

        val result = runExpectedSuccessTask("dependencies")

        val file = getResourceFile("dependencies/standalone-plugins-standalone-plugin-one-dependencies.txt")
        val expectedDependencies = file.readText()

        /*
            The contents of this file don't include shifting behaviour.

            Meaning: These lines should never change unless a configuration is updated or modified in a way
            that affects dependencies

            Lines slightly modified but are okay were:
                115 original - +--- project standalone-plugin-one (n)
                115 new      - +--- project standalone-plugins (n)

                127 original - +--- project :standalone-plugin-one (*)
                127 new      - +--- project standalone-plugins (n)

                179 original - +--- project :standalone-plugins:standalone-plugin-one (*)
                179 new      - +--- project : (*)

                283 original - \--- org.jetbrains.kotlin:kotlin-stdlib:1.9.21 (n)
                283 new      - \--- org.jetbrains.kotlin:kotlin-stdlib (n)
         */

        val comparableLines = getComparableBuildResultLines(result, 5, 9)

        Assertions.assertLinesMatch(expectedDependencies.lines(), comparableLines)
    }

}