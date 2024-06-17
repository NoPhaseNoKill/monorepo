package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class BaseKotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("com.nophasenokill.kotlin-dsl-plugin")
        target.pluginManager.apply("com.nophasenokill.repositories-plugin")
        target.pluginManager.apply("com.nophasenokill.group-and-version-plugin")
        target.pluginManager.apply("com.nophasenokill.publishing-plugin")
        target.pluginManager.apply("com.nophasenokill.java-gradle-applier-plugin")
        target.logger.quiet("Applied ${this::class.simpleName}")
    }
}