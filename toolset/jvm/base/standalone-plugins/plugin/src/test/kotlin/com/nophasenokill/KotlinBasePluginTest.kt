package com.nophasenokill

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class KotlinBasePluginTest {
    @Test
    fun `task number should stay the same when registering multiple plugins`() {

        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.nophasenokill.kotlin-base-plugin")
        val afterFirst = project.tasks.size

        project.plugins.apply("com.nophasenokill.kotlin-base-plugin")
        val afterSecond = project.tasks.size

        val expected = 21

        Assertions.assertEquals(expected, afterFirst)
        Assertions.assertEquals(expected, afterSecond)
        Assertions.assertEquals(afterFirst, afterSecond)
    }
}