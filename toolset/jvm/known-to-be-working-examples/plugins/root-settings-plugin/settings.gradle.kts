rootProject.name = "root-settings-plugin"

pluginManagement {
    includeBuild("../build-service-warning-fix-plugin")
    repositories.mavenCentral()
    repositories.gradlePluginPortal()
}

dependencyResolutionManagement {
    repositories.mavenCentral()
    repositories.gradlePluginPortal()
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")