package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class MetaPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.logger.quiet("Applied ${this::class.simpleName}")


        target.plugins.apply("com.nophasenokill.application")
    }
}