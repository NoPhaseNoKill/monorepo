package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class ExtendedPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        project.tasks.named("printSharedConfiguration") {

            val sharedConfig = project.configurations.findByName("sharedConfiguration")

            project.logger.quiet(sharedConfig.toString())

        }

    }
}