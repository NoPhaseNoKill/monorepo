
pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.20"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

includeBuild("platforms")
includeBuild("build-logic")

// includeBuild("libraries")
// includeBuild("applications")