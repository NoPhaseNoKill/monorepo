rootProject.name = "build-logic"


dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

includeBuild("../build-logic-meta")
include("meta-gradle-utilities")
include("kotlin-plugins")
include("structural-plugins")


enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")