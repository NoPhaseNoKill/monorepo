import java.util.*

val currentConfigurationCacheValueTask = tasks.register<CurrentConfigurationCacheValueTask>("currentConfigurationCacheValueTask") {

val currentConfigurationCacheValueTask = tasks.register<CurrentConfigurationCacheValueTask>("currentConfigurationCacheValue") {
    configurationCacheFromPropertiesValue.set(providers.gradleProperty("org.gradle.configuration-cache"))
    configurationCacheInfoFile.set(layout.buildDirectory.file("generated-resources/configurationCacheFromPropertiesValue.properties"))

    outputs.upToDateWhen { false }

    doLast {

        val indent = "      "
        val outputText = configurationCacheInfoFile.get().asFile.readText()
        val isConfigurationCacheOn = outputText.contains("isConfigurationCacheOn=true")
        logger.lifecycle("$indent *** REMINDER ***\n $indent $indent | Configuration cache key is set to: ~~~$isConfigurationCacheOn~~~\n $indent $indent | You will experience: ")

        if(isConfigurationCacheOn) {
            logger.lifecycle("$indent $indent $indent| -Issues With Running Dependencies Task In Intellij")
            logger.lifecycle("$indent $indent $indent| -But Tests Will Also Be Much Faster")
        } else {
            logger.lifecycle("$indent $indent $indent| -Significant Slowness")
            logger.lifecycle("$indent $indent $indent| -But You Will Be Able To Run Intellij Dependencies Task")
        }

        logger.lifecycle("$indent *** SEE GRADLE.PROPERTIES FOR MORE DETAILS ***")
    }
}


abstract class CurrentConfigurationCacheValueTask : DefaultTask() {
    @get:Input
    abstract val configurationCacheFromPropertiesValue: Property<String>

    @get:OutputFile
    abstract val configurationCacheInfoFile: RegularFileProperty

    @TaskAction
    fun writeVersionInfo() {
        val properties = Properties()

        val value = configurationCacheFromPropertiesValue.get()
        val isConfigurationCacheOn = value == "true"

        properties.setProperty("isConfigurationCacheOn", isConfigurationCacheOn.toString())

        configurationCacheInfoFile.get().asFile.outputStream().use { out ->
            properties.store(out, null)
        }
    }
}


tasks.register("testAll") {
    group = "verification"
    description = "Runs all the tests for every module."

    dependsOn(
        ":modules:libraries:list:test",
        ":modules:libraries:utilities:test",
        ":modules:applications:app:test",
        ":build-logic:plugins-new:plugin:check"
    )

    finalizedBy(currentConfigurationCacheValueTask)
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