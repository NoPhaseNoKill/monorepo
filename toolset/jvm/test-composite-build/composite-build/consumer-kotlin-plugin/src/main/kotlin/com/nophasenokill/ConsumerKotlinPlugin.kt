package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class ConsumerKotlinPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.afterEvaluate {
            target.pluginManager.apply("com.nophasenokill.base-kotlin-plugin")
            target.logger.quiet("Applied ${this::class.simpleName}")
        }
    }
}