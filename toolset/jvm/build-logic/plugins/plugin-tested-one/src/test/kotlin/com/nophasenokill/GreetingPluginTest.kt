package com.nophasenokill

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull


class GreetingPluginTest {
    @Test fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.nophasenokill.greetingPlugin")

        // Verify the result
        assertNotNull(project.tasks.findByName("taskInsideGreetingPlugin"))
        assertNull(project.tasks.findByName("taskInsideGreetingPlugin2"))
    }
}
