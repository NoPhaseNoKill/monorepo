package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class ProducerPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.logger.quiet("Ohai")
    }
}