pluginManagement {
    // Get community plugins from the Gradle Plugin Portal
    repositories.gradlePluginPortal()

    when(rootProject.name) {
        "jvm" -> {
            includeBuild("build-logic/plugins")
        }
        else -> {
            alertUserToMisconfiguration("pluginManagement")
        }
    }
}

dependencyResolutionManagement {
    // Allows retrieval of actual dependencies declared by the platform
    repositories.mavenCentral()

    when(rootProject.name) {
        "jvm" -> {
            println("Dependency management for project: ${rootProject.name} is including build:includeBuild(\"build-logic/platforms\")")
            includeBuild("build-logic/platforms")
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
        
        The only project that should be attempting this is 'jvm' which is the root of the multi-project, composite build.
        
        Most likely causes are:
            - Cause 1: Applying 'kotlin-project-root-settings' plugin to anything EXCEPT the settings.gradle.kts inside of jvm root dir
            - Cause 2: You have changed the jvm root dir settings file in a way which now includes another project's settings file.
        
        If you need a starting point, try looking at: '${rootProject.projectDir.toPath()}/settings.gradle.kts'.
        
        
    """.trimIndent()
    throw Exception(message)
}