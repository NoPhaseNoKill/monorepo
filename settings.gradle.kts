dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    includeBuild("modules")
}

// see: https://docs.gradle.org/current/userguide/declaring_dependencies.html#sec:type-safe-project-accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "integraboost"

// recommended way of defining shared logic for build locations components (through custom plugins)
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    includeBuild("platforms")
    includeBuild("build-logic")

}

includeBuild("modules")
