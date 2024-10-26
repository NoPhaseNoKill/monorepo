dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.develocity").version("3.18.1")
    id("org.gradle.toolchains.foojay-resolver-convention").version("0.8.0")
}

include("build-environment")

rootProject.name = "build-logic-settings"

apply(from = "../gradle/shared-with-buildSrc/mirrors.settings.gradle.kts")
