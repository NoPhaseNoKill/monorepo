println("Initializing settings.gradle.kts for ${rootProject.name}")

pluginManagement {
    println("pluginManagement for: ${rootProject.name}")
    repositories {
        includeBuild("../plugins")
    }
}

dependencyResolutionManagement {
    println("dependencyResolutionManagement for: ${rootProject.name}")
    repositories.gradlePluginPortal()
}

rootProject.name = "platform"