plugins {
    `kotlin-dsl`
}

evaluationDependsOnChildren()

gradlePlugin {
    plugins {
        create("kotlinDslPlugin") {
            id = "com.nophasenokill.kotlin-dsl-plugin"
            implementationClass = "com.nophasenokill.KotlinDslPlugin"
        }
    }
}

/*
     Fixes undeclared build service usage when using: enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
     Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165

     Note: Because of the structure of the whole root jvm project, this ALSO needs to be applied directly
     to this level too, as well as exposing a re-usable plugin to fix the SAME issue for other included builds
     or projects.
 */

// afterEvaluate {
//     // Iterate over specific tasks or use a pattern to select tasks
//     val tasksOfInterest = project.tasks.withType(DefaultTask::class.java)
//
//     tasksOfInterest.configureEach {
//         // Directly interact with the service registrations relevant to Kotlin compilation tasks
//         project.gradle.sharedServices.registrations.all {
//             val buildServiceProvider = this.service
//             val buildService = buildServiceProvider.get()
//
//             val kotlinCollectorSearchString = "org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector"
//             if (buildService.toString().contains(kotlinCollectorSearchString)) {
//                 project.logger.debug(
//                     "Applying checkKotlinGradlePluginConfigurationErrors workaround to task: {} for project: {}",
//                     this@configureEach.name,
//                     project.name
//                 )
//                 this@configureEach.usesService(buildServiceProvider)
//             }
//         }
//     }
// }

dependencies {
    implementation("org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:4.4.0")
}