
rootProject.name = "applications"

pluginManagement {
    includeBuild("../meta-plugins") {
        name = "meta-plugins"
    }
    repositories.gradlePluginPortal()
}

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

include("application-one")
includeBuild("../meta-plugins")
includeBuild("../standalone-plugins")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")