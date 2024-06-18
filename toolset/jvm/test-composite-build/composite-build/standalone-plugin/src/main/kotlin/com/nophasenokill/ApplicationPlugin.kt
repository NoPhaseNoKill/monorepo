package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class ApplicationPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.logger.quiet("Applied ${this::class.simpleName}")
    }
}
