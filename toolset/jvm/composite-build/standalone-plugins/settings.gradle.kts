
rootProject.name = "standalone-plugins"

pluginManagement {
    includeBuild("../meta-plugins") {
        name = "meta-plugins"
    }
    repositories.gradlePluginPortal()
}

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

include("standalone-plugin-one")


enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
