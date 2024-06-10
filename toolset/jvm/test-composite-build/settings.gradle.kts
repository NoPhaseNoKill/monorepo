
rootProject.name = "test-composite-build"

pluginManagement {
    repositories {
        includeBuild("composite-build/base-plugins/root-settings-plugin")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

// include(":root-settings-plugin")
// project(":root-settings-plugin").projectDir = file("composite-build/base-plugins/root-settings-plugin")

include(":plugin-template-plugin")
project(":plugin-template-plugin").projectDir = file("composite-build/base-plugins/plugin-template-plugin")


include(":producerPlugin")
project(":producerPlugin").projectDir = file("composite-build/meta-plugins/producerPlugin")


include(":consumerPlugin")
project(":consumerPlugin").projectDir = file("composite-build/standard-plugins/consumerPlugin")


plugins {
    id("com.nophasenokill.root-settings-plugin")
}

