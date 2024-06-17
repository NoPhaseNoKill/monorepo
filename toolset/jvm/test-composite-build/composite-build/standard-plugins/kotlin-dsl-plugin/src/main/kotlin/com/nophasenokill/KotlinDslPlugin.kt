package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project


class KotlinDslPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("org.jetbrains.kotlin.jvm")
        target.logger.quiet("Applied ${this::class.simpleName}")
    }
}