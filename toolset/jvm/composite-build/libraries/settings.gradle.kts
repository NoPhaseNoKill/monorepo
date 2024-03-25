

pluginManagement {
    includeBuild("../standalone-plugins")
    repositories.gradlePluginPortal()
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "libraries"

include("library-one")
include("library-two")



