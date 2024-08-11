rootProject.name = "build-logic-meta"

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

include("kotlin-dsl-plugin")

settings.enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
settings.enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")