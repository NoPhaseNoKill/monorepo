package com.nophasenokill

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildServiceWarningFixPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        /*
             Fixes undeclared build service usage when using: enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
             Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165
         */

            project.afterEvaluate {
                project.run {
                // Iterate over specific tasks or use a pattern to select tasks
                val tasksOfInterest = project.tasks.withType(DefaultTask::class.java)

                tasksOfInterest.configureEach {
                    // Directly interact with the service registrations relevant to Kotlin compilation tasks
                    project.gradle.sharedServices.registrations.all {
                        val buildServiceProvider = this.service
                        val buildService = buildServiceProvider.get()

                        val kotlinCollectorSearchString = "org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector"
                        if (buildService.toString().contains(kotlinCollectorSearchString)) {
                            project.logger.debug(
                                "Applying checkKotlinGradlePluginConfigurationErrors workaround to task: {} for project: {}",
                                this@configureEach.name,
                                project.name
                            )
                            this@configureEach.usesService(buildServiceProvider)
                        }
                    }
                }
            }
        }
    }
}