
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention")
}

pluginManagement {
    repositories {
        maven {
            url = uri("${rootProject.projectDir}/local-repo/com/nophasenokill/producer-plugin")
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}