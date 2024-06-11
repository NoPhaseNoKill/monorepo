package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class ConsumerPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.afterEvaluate {
            target.plugins.apply("com.nophasenokill.producer")
        }
    }
}