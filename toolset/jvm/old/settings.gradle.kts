rootProject.name = "build-logic"

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
    }
    includeBuild("platforms")
}

include("plugins")