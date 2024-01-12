pluginManagement {
    includeBuild("../settings")
}

dependencyResolutionManagement {
    // allows for delegation of dependency resolution discovery to each of the platform plugins
    repositories.gradlePluginPortal()
}

plugins {
    id("kotlin-project-root-settings")
}

rootProject.name = "platforms"

include("plugins-platform")
include("test-platform")