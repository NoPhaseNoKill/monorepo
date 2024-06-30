package com.nophasenokill

import org.gradle.api.Project

class KotlinLibraryPlugin: KotlinBase() {
    override fun apply(project: Project) {

        super.apply(project)

        project.pluginManager.apply("java-library")
    }
}