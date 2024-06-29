
rootProject.name = "build-service-warning-fix-plugin"

pluginManagement {

    /*
        Cannot include root-settings-plugin here, because it is a pre-requisite for it.
        Due to root-settings-plugin being easier out of the two plugins to duplicate,
        I have decided to just manually do the equivalent in here.
    */

    plugins {
        id("org.jetbrains.kotlin.jvm") version("1.9.22")
    }
    repositories.mavenCentral()
    repositories.gradlePluginPortal()
}

dependencyResolutionManagement {
    repositories.mavenCentral()
    repositories.gradlePluginPortal()
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")