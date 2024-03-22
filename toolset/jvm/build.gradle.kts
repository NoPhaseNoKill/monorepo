import java.util.*

val currentConfigurationCacheValueTask = tasks.register<CurrentConfigurationCacheValueTask>("currentConfigurationCacheValueTask") {

    configurationCacheFromPropertiesValue.set(providers.gradleProperty("org.gradle.configuration-cache").getOrElse("false"))
    configurationCacheInfoFile.set(layout.buildDirectory.file("generated-resources/configurationCacheFromPropertiesValue.properties"))

    outputs.upToDateWhen { false }
    outputs.cacheIf { false }

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

val buildPluginsTask = tasks.register("buildPluginsTask") {
    group = "verifications"
    description = "Builds all plugins"

    dependsOn(gradle.includedBuild("standalone-plugins").task(":plugin:build"))
}

val checkAllTask = tasks.register("checkAllTask") {
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:check"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:check"))
    dependsOn(gradle.includedBuild("modules").task(":applications:app:check"))
    dependsOn(gradle.includedBuild("modules").task(":applications:accelerated-test-suite-runner:check"))
}


tasks.register("check") {
    group = "verification"
    description = "Run all checks"

    dependsOn(buildPluginsTask)
    dependsOn(checkAllTask)
    finalizedBy(currentConfigurationCacheValueTask)
}