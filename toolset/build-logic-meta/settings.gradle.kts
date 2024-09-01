rootProject.name = "build-logic-meta"

pluginManagement {
    repositories.gradlePluginPortal()
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

settings.enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
settings.enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
