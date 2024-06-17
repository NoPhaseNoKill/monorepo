package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class JavaGradleApplierPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("org.gradle.java-gradle-plugin")
        target.logger.quiet("Applied ${this::class.simpleName}")
    }
}