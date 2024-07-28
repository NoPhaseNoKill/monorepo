package com.nophasenokill

import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Provider

object GradleDirectory {
    fun Project.getBuildDirectory(): DirectoryProperty {
        return layout.buildDirectory
    }
}