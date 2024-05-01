package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.libsDirectory

class CheckKotlinBuildServiceFixPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        /*
             Fixes undeclared build service usage when using: enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
             Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165
         */
        project.gradle.taskGraph.whenReady {
            project.tasks.named("checkKotlinGradlePluginConfigurationErrors").configure { task ->
                val kotlinCollectorSearchString = "org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector"
                project.gradle.sharedServices.registrations.all { buildServiceRegistration ->
                    val buildServiceProvider = buildServiceRegistration.service
                    val buildService = buildServiceProvider.get()
                    val isCollectorService = buildService.toString().contains(kotlinCollectorSearchString)

                    if(isCollectorService) {
                        project.logger.debug(
                            "Applying checkKotlinGradlePluginConfigurationErrors workaround to task: {} for project: {}",
                            task,
                            project.name
                        )
                        task.usesService(buildServiceProvider)
                    }
                }
            }
        }
    }
}