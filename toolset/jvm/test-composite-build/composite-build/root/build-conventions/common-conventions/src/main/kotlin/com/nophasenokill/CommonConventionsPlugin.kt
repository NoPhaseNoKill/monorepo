package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class CommonConventionsPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.afterEvaluate {
            target.logger.quiet("com.nophasenokill.common-conventions applied")
        }
    }
}