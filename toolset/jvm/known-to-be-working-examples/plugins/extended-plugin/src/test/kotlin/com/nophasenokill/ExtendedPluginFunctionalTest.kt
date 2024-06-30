package com.nophasenokill

import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class ExtendedPluginFunctionalTest: PluginTest() {

    @Test
    fun `should be able to build plugin, with java 21, and run task which comes from a plugin that this plugin is dependant on`() {

        buildFile.appendText(
            """
                plugins {
                    id("com.nophasenokill.extended-plugin")
                }
            """
        )

        val buildResult = runTask("build")
        val hashSourceResult = runTask("hashSource")
        val checkJavaVersionResult = runTask("checkJavaVersion")

        Assertions.assertEquals(TaskOutcome.SUCCESS, buildResult.task(":build")?.outcome)
        Assertions.assertEquals(TaskOutcome.SUCCESS, hashSourceResult.task(":hashSource")?.outcome)
        Assertions.assertEquals(TaskOutcome.SUCCESS, checkJavaVersionResult.task(":checkJavaVersion")?.outcome)
    }
}