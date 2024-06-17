package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project


class GroupAndVersionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.group = "com.nophasenokill"
        target.version = "1.0.0-local-dev"
        target.logger.quiet("Applied ${this::class.simpleName}")
    }
}