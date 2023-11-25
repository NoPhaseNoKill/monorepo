dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    includeBuild("../platforms")
}

rootProject.name = "build-logic"
include("commons")
include("java-library")
include("kotlin-library")