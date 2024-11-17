package com.nophasenokill

import com.nophasenokill.GradleDirectory.getBuildDirectory
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Paths


class GradleDirectoryTest {
    
    @Test
    fun `should calculate build directory correctly`() {
        val project = getProject()
        val rootDir = project.rootDir
        val expectedBuildDir = project.file(Paths.get(rootDir.absolutePath, "/build"))
        val actual = project.getBuildDirectory().map { it.asFile.absolutePath }

        Assertions.assertEquals(expectedBuildDir.path, actual.get())
    }

    private fun getProject(): Project {
        return ProjectBuilder.builder().build()
    }
}

