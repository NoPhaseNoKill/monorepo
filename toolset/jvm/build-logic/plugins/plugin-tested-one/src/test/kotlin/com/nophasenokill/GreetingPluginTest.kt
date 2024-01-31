package com.nophasenokill

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test


class GreetingPluginTest {
    @Test
    fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.nophasenokill.greetingPlugin")

        // Verify the result
        assertNotNull(project.tasks.findByName("taskInsideGreetingPlugin"))
        assertNull(project.tasks.findByName("taskInsideGreetingPlugin2"))
    }
}
