
rootProject.name = "platforms"

pluginManagement {
    repositories.gradlePluginPortal()
}

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

include("generalised-platform")
include("junit-platform")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

