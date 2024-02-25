plugins {
    `kotlin-dsl` apply false

    /*
        Configures the dependency analysis plugin for all subprojects (libraries/applications),
        which allows us to run './gradle check' that invokes the checkDependencyFormattingProject
        task.

        See: 'dependency-analysis-project' plugin for details
     */
    // id("dependency-analysis-plugin")
}

/*
    Fixes undeclared build service usage when using: enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
    Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165
 */
// gradle.allprojects {
//     gradle.sharedServices.registrations.all {  ->
//         if(this.service.get().toString().contains("org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector")) {
//             val collectorService: Provider<out BuildService<out BuildServiceParameters>> = this.service
//
//             tasks.withType(DefaultTask::class.java).configureEach {
//                 usesService(collectorService)
//             }
//         }
//     }
// }
//
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



group = "com.nophasenokill.modules"