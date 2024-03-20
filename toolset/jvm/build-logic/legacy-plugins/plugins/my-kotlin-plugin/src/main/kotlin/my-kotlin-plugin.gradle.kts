plugins {
    kotlin("jvm")
}

dependencies {
    // // enforces that versions from each of the boms are used
    // implementation(platform("com.nophasenokill.platform:platform"))
    // implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // applies standard kotlin libs to projects
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    // runtimeOnly(kotlin("reflect"))
}
//
// // used here because scoping means that it can't be used inside
// val mainSourceSetOutputProvider = sourceSets.main.map { it.output }
//
// kotlin {
//     // kotlinDaemonJvmArgs = listOf("-Xmx486m", "-Xms256m", "-XX:+UseParallelGC")
//
//     /*
//         Ensures that jar is included properly for anyone who overrides destination folder
//         See: https://kotlinlang.org/docs/gradle-configure-project.html#non-default-location-of-compile-tasks-destinationdirectory
//     */
//     tasks.jar {
//
//         val mainSourceSetKotlinClasses = sourceSets.main.map { it.kotlin.classesDirectory }
//
//         from(mainSourceSetOutputProvider)
//         from(mainSourceSetKotlinClasses)
//     }
//
//     /*
//         Adds an output to task checkKotlinGradlePluginConfigurationErrors, which allows
//         us to skip it if none of the inputs/outputs change.
//      */
//     tasks.named("checkKotlinGradlePluginConfigurationErrors").configure {
//
//         val output: String =
//             if (state.failure != null) {
//                 "Configuration error detected: ${state.failure?.message}"
//             } else {
//                 "Check Kotlin Gradle Plugin Configuration Errors task was successful."
//             }
//
//         val outputDir = layout.buildDirectory.dir("extendedCheckKotlinGradlePluginConfigurationErrors")
//         val outputFile = outputDir.map {
//             it.file("extendedCheckKotlinGradlePluginConfigurationErrorsResult.txt")
//         }
//         outputs.file(outputFile)
//
//         doLast {
//             outputFile.get().asFile.writeText("") // clear file contents
//             outputFile.get().asFile.appendText(output)
//         }
//     }
//
//     //     /*
//     //         Uncomment this + the properties in gradle.properties to run using k2 compiler
//     //      */
//     //     // sourceSets.all {
//     //     //     languageSettings {
//     //     //         languageVersion = "2.0"
//     //     //     }
//     //     // }
//     //
//     //     /*
//     //         Uncomment this + targetCompatibility in the java extension if you need to verify
//     //         that the default setting: jvmTargetValidationMode = JvmTargetValidationMode.ERROR
//     //         is worked (default behaviour)
//     //      */
//     //     // compilerOptions {
//     //     //     jvmTarget = JvmTarget.JVM_1_8
//     //     // }
// }
//
//
// tasks.register("compileAll") {
//     group = LifecycleBasePlugin.BUILD_GROUP
//     description = "Compile all Java code"
//
//     dependsOn(tasks.check)
// }
//
// tasks.register("printDependencies") {
//     group = LifecycleBasePlugin.VERIFICATION_GROUP
//     description = "Prints dependencies by configuration"
//
//     configurations.forEach { config ->
//         if (config.isCanBeResolved) {
//             logger.lifecycle("\nConfiguration dependencies: ${config.name}")
//             try {
//                 config.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
//                     logger.lifecycle(" - ${artifact.moduleVersion.id.group}:${artifact.name}:${artifact.moduleVersion.id.version}")
//                 }
//             } catch (e: Exception) {
//                 logger.lifecycle("Failed to resolve ${config.name}: ${e.message}")
//             }
//         }
//     }
// }