pluginManagement {
    includeBuild("../settings")
}

dependencyResolutionManagement {
    // allows for delegation of dependency resolution discovery to each of the platform plugins
    repositories.gradlePluginPortal()
}

rootProject.name = "platforms"

include("plugins-platform")
include("test-platform")