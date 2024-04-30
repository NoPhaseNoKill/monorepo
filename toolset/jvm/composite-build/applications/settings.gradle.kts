
rootProject.name = "applications"

pluginManagement {
    includeBuild("../standalone-plugins/standalone-plugin-one")
}

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

include("application-one")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")