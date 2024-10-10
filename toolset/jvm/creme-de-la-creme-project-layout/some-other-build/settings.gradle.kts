
rootProject.name = "some-other-build"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        id("root-settings")
    }

    includeBuild("../build-logic")
}

plugins {
    id("root-settings")
}
