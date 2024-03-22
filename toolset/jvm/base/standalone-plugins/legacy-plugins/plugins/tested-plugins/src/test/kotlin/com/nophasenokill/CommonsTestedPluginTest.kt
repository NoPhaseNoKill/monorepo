package com.nophasenokill

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test


class CommonsTestedPluginTest {
    @Test
    fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("commons-tested-plugin")

        // Verify the result
        assertNotNull(project.tasks.findByName("taskInsideCommonsTestedPlugin"))
        assertNull(project.tasks.findByName("taskInsideCommonsTestedPlugin2"))
    }
}
