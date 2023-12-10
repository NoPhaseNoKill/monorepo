
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
    }
}

includeBuild("../build-logic-platforms")


rootProject.name = "build-logic"
include("commons")
include("java-library")
include("kotlin-library")
include("kotlin-application")