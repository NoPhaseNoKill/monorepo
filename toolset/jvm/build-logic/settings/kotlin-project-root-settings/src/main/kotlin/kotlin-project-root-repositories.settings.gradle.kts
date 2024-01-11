pluginManagement {
    // Get community plugins from the Gradle Plugin Portal
    repositories.gradlePluginPortal()

    println("****Respository directory:${File(rootDir, "").toPath()} ")

    println("Attempting to include: ${File(rootDir, "build-logic/plugins")}")
    // Get our own convention plugins when we're in modules sub-dirs
    if (File(rootDir, "plugins").exists()) {
        println("Including: ${File(rootDir, "build-logic/plugins")}")
        includeBuild("plugins")
    }
    println("Attempting to include: ${File(rootDir, "build-logic/plugins")}")
    // If not the main build, 'plugins' is located next to the build (e.g. gradle/settings)
    if (File(rootDir, "build-logic/plugins").exists()) {
        println("Including: ${File(rootDir, "build-logic/plugins")}")
        includeBuild("build-logic/plugins")
    }
}

dependencyResolutionManagement {
    // Get components from Maven Central
    repositories.mavenCentral()
    println("Attempting to include: ${File(rootDir, "build-logic/platforms")}")
    // In the main build, find the platform in 'gradle/platform'
    if (File(rootDir, "build-logic/platforms").exists()) {
        println("Including: ${File(rootDir, "build-logic/platforms")}")
        includeBuild("build-logic/platforms")
    }
}