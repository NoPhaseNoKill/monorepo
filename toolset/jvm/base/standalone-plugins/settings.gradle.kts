pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
    }

    versionCatalogs {
        create("libs") {
            from(files("../../gradle/libs.versions.toml"))
        }
    }
}

/*
    Fixes undeclared build service usage when using: enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
    Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165
 */
gradle.allprojects {
    val projectName = this.name
    gradle.sharedServices.registrations.all {  ->
        if(this.service.get().toString().contains("org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector")) {
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


rootProject.name = "standalone-plugins"

include("plugin")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")