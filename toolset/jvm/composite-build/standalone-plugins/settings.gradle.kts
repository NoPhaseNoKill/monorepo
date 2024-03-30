
rootProject.name = "standalone-plugins"

pluginManagement {
    repositories.gradlePluginPortal()
}

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

include("standalone-plugin-one")
includeBuild("../platforms")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
