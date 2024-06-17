package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class RepositoriesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.repositories.mavenCentral()
        target.repositories.gradlePluginPortal()
        target.logger.quiet("Applied ${this::class.simpleName}")
    }
}