pluginManagement {
    repositories {
        includeBuild("../plugins")
    }
}

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

rootProject.name = "platform"