

pluginManagement {
    includeBuild("../build-logic-commons")
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

apply(from = "../gradle/shared-with-buildSrc/mirrors.settings.gradle.kts")

include("build-update-utils")
include("root-build")
include("cleanup")
include("idea")
include("lifecycle")

rootProject.name = "build-logic"
