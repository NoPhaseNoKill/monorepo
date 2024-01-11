pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    includeBuild("../platforms")
}

rootProject.name = "settings"

include("kotlin-project-root-settings")