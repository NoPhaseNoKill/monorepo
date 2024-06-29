package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class ExtendedPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        /*
            This has transitives of:
                - domain:person (relative to root)
                - domain:account (relative to root)
         */
        project.apply {
            plugin("com.nophasenokill.hash-source-plugin")
        }

        project.tasks.named("printSharedConfiguration") {

            val sharedConfig = project.configurations.findByName("sharedConfiguration")

            project.logger.quiet(sharedConfig.toString())

        }

    }
}