
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention")
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}