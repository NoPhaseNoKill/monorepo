rootProject.name = "build-logic"

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}


include("kotlin-plugins")
include("structural-plugins")


enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")