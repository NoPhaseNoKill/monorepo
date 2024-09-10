rootProject.name = "build-logic"



pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
}

include("meta-gradle-utilities")
include("kotlin-plugins")
include("structural-plugins")


enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
