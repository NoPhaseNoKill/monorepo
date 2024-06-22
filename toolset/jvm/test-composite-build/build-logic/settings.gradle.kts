pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.20"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

includeBuild("../platforms")

rootProject.name = "build-logic"
include("commons")
include("kotlin-library")
include("kotlin-application")
