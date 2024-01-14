pluginManagement {
    /*
        Unsure whether we actually need this line, works fine without, but may be needed in the future.
        Was previously used to get community plugins.
     */
    repositories.gradlePluginPortal()

    when(rootProject.name) {
        "jvm" -> {
            includeBuild("build-logic/plugins")
        }
        "platform" -> {
            includeBuild("../plugins")
        }
        "plugins" -> {
            // already have everything we need
        }
        else -> {
            alertUserToMisconfiguration("pluginManagement")
        }
    }
}

dependencyResolutionManagement {
    when(rootProject.name) {
        "jvm" -> {
            repositories.mavenCentral() // Allows retrieval of actual dependencies declared by the platform
            includeBuild("build-logic/platform")
        }
        "platform" -> {
            // allows for delegation of dependency resolution discovery to each of the platform plugins
            repositories.gradlePluginPortal()
        }
        "plugins" -> {
            // explicitly gradle plugin portal because we only want to search for our convention plugins,
            // where the convention plugins delegate the dependency retrieval to the platform
            repositories.gradlePluginPortal()
            includeBuild("../platform")
        }
        else -> {
            alertUserToMisconfiguration("dependencyResolutionManagement")
        }
    }
}

@Throws(Exception::class)
fun alertUserToMisconfiguration(managementType: String) {
    /*
        Two line before/two line after message is intentional so error is easier to read due to gradle formatting being
        difficult to read in terminal. Glance value becomes hard otherwise.
     */
    val message = """
        
        
        Attempted '${managementType}' for unknown root project: '${rootProject.name}'.
        
        The only projects that should be attempting this are:
            - jvm
            - plugins
            - platform 
        
        If you have added a new project (that is NOT in modules/libraries or modules/applications) which requires 
        access to 'kotlin-project-root-settings', please configure this inside of: 
        
            kotlin-project-root-repositories.settings.gradle.kts.
        
        Otherwise, most likely causes are:
            - Cause 1: Applying 'kotlin-project-root-settings' plugin to anything OTHER THAN (jvm|plugins|platform) settings' files
            - Cause 2: You have changed any of the (jvm|plugins|platform) settings' files in a way which now includes another project's settings file.
        
        If you need a starting point, try looking at: '${rootProject.projectDir.toPath()}/settings.gradle.kts'.
        
        
    """.trimIndent()
    throw Exception(message)
}