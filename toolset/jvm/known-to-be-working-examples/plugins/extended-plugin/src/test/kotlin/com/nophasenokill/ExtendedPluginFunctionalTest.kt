package com.nophasenokill

import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis


class ExtendedPluginFunctionalTest: PluginTest() {

    @Test
    fun canBeBuilt() {

       val time = measureTimeMillis {
            buildFile.appendText(
                """
                plugins {
                    id("com.nophasenokill.extended-plugin")
                }
            """
            )


            val result = runTask("build")
            Assertions.assertEquals(TaskOutcome.SUCCESS, result.task(":build")?.outcome)
        }

        println("Time test took was: ${time}")

    }
}