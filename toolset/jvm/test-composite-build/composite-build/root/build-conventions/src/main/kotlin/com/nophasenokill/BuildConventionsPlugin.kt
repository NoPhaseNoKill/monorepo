package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildConventionsPlugin: Plugin<Project> {
    override fun apply(target: Project) {

        target.afterEvaluate {
            target.pluginManager.apply("com.nophasenokill.java-conventions")
            target.pluginManager.apply("com.nophasenokill.common-conventions")

            target.logger.quiet("com.nophasenokill.build-conventions applied")
        }
    }
}