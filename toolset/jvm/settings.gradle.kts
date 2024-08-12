

rootProject.name = "jvm"

pluginManagement {

    val kotlinVersion = "2.0.0"

    buildscript {
        repositories {
            maven {
                url = uri("https://plugins.gradle.org/m2/")
            }
        }
        dependencies {
            classpath("org.jetbrains.kotlin:kotlin-serialization:${kotlinVersion}")
            classpath("org.jetbrains.kotlin:compose-compiler-gradle-plugin:${kotlinVersion}")
        }
    }

    repositories {

        /*
            This ensures absolute consistency across the composite build.
            Without this, we run into race conditions where included builds
            compete against each other and may load different versions.
            This should be the only version declared this way,
            the rest should be in the .toml file
         */

        plugins {
            id("org.gradle.kotlin.kotlin-dsl") version kotlinVersion
        }

        gradlePluginPortal()
        google()
        mavenCentral()
    }

    includeBuild("../build-logic")
    includeBuild("../build-logic-meta")
}
/*
    DO not use dependency management here. If you need to add more repositories,
    add them to: com.nophasenokill.component-plugin.settings.gradle.kts
 */

plugins {
    id("com.nophasenokill.component-plugin")
}

settings.enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
settings.enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
