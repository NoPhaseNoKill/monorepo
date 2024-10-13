rootProject.name = "meta-plugins"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()

    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()

    }
}

include("root-settings-plugin")
