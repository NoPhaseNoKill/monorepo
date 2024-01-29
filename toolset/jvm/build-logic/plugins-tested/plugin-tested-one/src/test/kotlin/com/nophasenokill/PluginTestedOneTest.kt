package com.nophasenokill

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * A simple unit test for the 'com.nophasenokill.greeting' plugin.
 */
class PluginTestedOneTest {
    @Test fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.nophasenokill.someTask")

        // Verify the result
        assertNotNull(project.tasks.findByName("someTask"))
    }
}
