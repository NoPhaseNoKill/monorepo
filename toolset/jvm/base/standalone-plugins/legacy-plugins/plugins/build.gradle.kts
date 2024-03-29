

 /*
     Fixes undeclared build service usage when using: enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
     Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165
  */
gradle.allprojects {
    val projectName = this.name
    gradle.sharedServices.registrations.all {  ->
        if(this.service.map { it.toString().contains("org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector") }.get()) {
            val collectorService: Provider<out BuildService<out BuildServiceParameters>> = this.service

            tasks.withType(DefaultTask::class.java).configureEach {
                if(this.name == "checkKotlinGradlePluginConfigurationErrors") {
                    logger.debug(
                        "Applying checkKotlinGradlePluginConfigurationErrors workaround to task: {} for project: {}",
                        this.name,
                        projectName
                    )
                    usesService(collectorService)
                }
            }
        }
    }
}
//
// configurations.all {
//     resolutionStrategy {
//         failOnVersionConflict()
//         /*
//             equivalent to both:
//                 - failOnDynamicVersions()
//                 - failOnChangingVersions()
//          */
//         failOnNonReproducibleResolution()
//     }
// }

group = "com.nophasenokill.plugins"