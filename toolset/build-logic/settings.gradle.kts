rootProject.name = "build-logic"

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

// plugins {
//     id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
// }

include("kotlin-plugins")
include("structural-plugins")



enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")