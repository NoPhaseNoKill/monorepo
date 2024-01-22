

println("Initializing build.gradle.kts for: $name")

/*
    Fixes undeclared build service usage when using: enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
    Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165
 */
gradle.allprojects {
    gradle.sharedServices.registrations.all {  ->
        if(this.service.get().toString().contains("org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector")) {
            val collectorService: Provider<out BuildService<out BuildServiceParameters>> = this.service

            tasks.withType(DefaultTask::class.java).configureEach {
                usesService(collectorService)
            }
        }
    }
}

group = "com.nophasenokill.plugins"