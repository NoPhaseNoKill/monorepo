package com.nophasenokill

import com.nophasenokill.functionalTest.FunctionalTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class SecondPlaceholderPluginFunctionalTest: FunctionalTest() {

    @Test
    fun `some gradle test for plugin two`() {
        Assertions.assertNotNull(projectDir)
        Assertions.assertNotNull(buildFile)
        Assertions.assertNotNull(settingsFile)
        Assertions.assertNotEquals(buildFile, settingsFile)
        Assertions.assertNotEquals(buildFile, projectDir)
        Assertions.assertNotEquals(settingsFile, projectDir)
    }

    @Test
    fun `secondary gradle test for plugin two`() {
        Assertions.assertNotNull(projectDir)
        Assertions.assertNotNull(buildFile)
        Assertions.assertNotNull(settingsFile)
        Assertions.assertNotEquals(buildFile, settingsFile)
        Assertions.assertNotEquals(buildFile, projectDir)
        Assertions.assertNotEquals(settingsFile, projectDir)
    }

    @Test
    fun `gradle test with shared name across test suite`() {
        Assertions.assertNotNull(projectDir)
        Assertions.assertNotNull(buildFile)
        Assertions.assertNotNull(settingsFile)
        Assertions.assertNotEquals(buildFile, settingsFile)
        Assertions.assertNotEquals(buildFile, projectDir)
        Assertions.assertNotEquals(settingsFile, projectDir)
    }

    @Test
    @Disabled
    fun `should keep tmp dir of gradle test run when test fails`() {
        Assertions.assertNotNull(projectDir)
        Assertions.assertNotNull(buildFile)
        Assertions.assertNotNull(settingsFile)
        Assertions.assertNotEquals(buildFile, settingsFile)
        Assertions.assertNotEquals(buildFile, projectDir)
        Assertions.assertNotEquals(settingsFile, projectDir)

        throw RuntimeException()
    }
}