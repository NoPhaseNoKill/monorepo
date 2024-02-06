

pluginManagement {
    repositories {
        includeBuild("../plugins")
    }
}

dependencyResolutionManagement {

    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "platform"

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")