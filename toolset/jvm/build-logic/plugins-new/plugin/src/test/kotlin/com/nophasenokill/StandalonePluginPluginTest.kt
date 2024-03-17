package com.nophasenokill

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class StandalonePluginPluginTest {

    @Test
    fun `plugin registers task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.nophasenokill.plugins-new.plugin")


        Assertions.assertNotNull(project.tasks.findByName("greeting"))
        println("Ohai")
    }
}
