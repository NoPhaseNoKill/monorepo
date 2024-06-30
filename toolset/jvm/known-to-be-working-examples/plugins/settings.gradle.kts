
rootProject.name = "plugins"

pluginManagement {
    includeBuild("root-settings-plugin")
}

plugins {
    id("com.nophasenokill.root-settings-plugin")
}

includeBuild("basic-plugin")
includeBuild("extended-plugin")
includeBuild("hash-source-plugin")
includeBuild("build-service-warning-fix-plugin")