package com.nophasenokill

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty

object GradleDirectory {
    fun Project.getBuildDirectory(): DirectoryProperty {
        return layout.buildDirectory
    }
}