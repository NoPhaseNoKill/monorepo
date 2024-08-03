rootProject.name = "build-logic-meta"

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

include("kotlin-dsl-plugin")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")