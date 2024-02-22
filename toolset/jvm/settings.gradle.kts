pluginManagement {
    includeBuild("build-logic/settings")
}

plugins {
    id("jvm-root-settings")
}

rootProject.name = "jvm"

includeBuild("modules")