pluginManagement {
    // Get community plugins from the Gradle Plugin Portal
    repositories.gradlePluginPortal()

    when(rootProject.name) {
        "jvm" -> {
            println("Plugin management for project: ${rootProject.name} is including a build: includeBuild(\"build-logic/plugins\")")
            includeBuild("build-logic/plugins")
        }
        "platforms" -> {
            println("Plugin management for project: ${rootProject.name} is including a build: includeBuild(\"../plugins\")")
            includeBuild("../plugins")
        }
        else -> throw Exception("Unknown root project. Please fix: kotlin-project-root-repositories.settings.gradle.kts")
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
        "platforms" -> {
            println("Dependency management for project: ${rootProject.name} is NOT including a build as it should already be configured")
        } // do nothing as platforms already configured
        else -> throw Exception("Unknown root project. Please fix: kotlin-project-root-repositories.settings.gradle.kts")
    }
}