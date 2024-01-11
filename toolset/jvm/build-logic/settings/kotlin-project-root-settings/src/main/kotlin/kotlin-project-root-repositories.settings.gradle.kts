pluginManagement {
    // Get community plugins from the Gradle Plugin Portal
    repositories.gradlePluginPortal()

    println("****Plugin management, root dir:${File(rootDir, "").toPath()} ")
    val path = File(rootDir, "").toPath()

    if(path.toString() == "/home/tomga/projects/monorepo/toolset/jvm") {
        println("Including Plugin: ${File(rootDir, "build-logic/plugins")}")
        includeBuild("build-logic/plugins")
    } else {
        println("Including Plugin: ${File(rootDir, "../plugins").path}")
        includeBuild("../plugins")
    }
    //
    // println("Attempting to include: ${File(rootDir, "build-logic/plugins")}")
    // // Get our own convention plugins when we're in modules sub-dirs
    // if (File(rootDir, "plugins").exists()) {
    //     println("Including: ${File(rootDir, "build-logic/plugins")}")
    //     includeBuild("plugins")
    // }
    // println("Attempting to include: ${File(rootDir, "build-logic/plugins")}")
    // // If not the main build, 'plugins' is located next to the build (e.g. gradle/settings)
    // if (File(rootDir, "build-logic/plugins").exists()) {
    //     println("Including: ${File(rootDir, "build-logic/plugins")}")
    //     includeBuild("build-logic/plugins")
    // }
}

dependencyResolutionManagement {
    // Get components from Maven Central
    repositories.mavenCentral()

    println("****Dependency management, root dir:${File(rootDir, "").toPath()} ")
    val path = File(rootDir, "").toPath()

    if(path.toString() == "/home/tomga/projects/monorepo/toolset/jvm") {
        println("Including Dependency: ${File(rootDir, "build-logic/platforms")}")
        includeBuild("build-logic/platforms")
    }
}