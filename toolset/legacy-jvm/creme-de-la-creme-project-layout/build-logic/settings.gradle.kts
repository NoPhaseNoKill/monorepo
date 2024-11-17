
rootProject.name = "build-logic"



pluginManagement {

    val lines = file("../../kotlin-meta-dependencies.txt").readLines()
    val kotlinVersion = lines.first { it.contains("kotlinJvmVersion") }.substringAfter("=").trimStart()
    val foojayResolverVersion = lines.first { it.contains("fooJayResolverVersion") }.substringAfter("=").trimStart()

    repositories {
        gradlePluginPortal()
    }
    plugins {
        println("Applying standard jvm version $kotlinVersion across any included builds and subprojects")
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.gradle.toolchains.foojay-resolver-convention") version foojayResolverVersion
    }
}

gradle.lifecycle.beforeProject {

    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

gradle.lifecycle.afterProject {
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

include("convention-plugins")
include("binary-plugins")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


