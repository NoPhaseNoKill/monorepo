package com.nophasenokill


import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class RootSettingsPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        println("Applied ${this::class.simpleName}")

        target.pluginManagement {
            repositories {
                gradlePluginPortal()
                mavenCentral()
            }
        }

        target.dependencyResolutionManagement {
            repositories {
                gradlePluginPortal()
                mavenCentral()
            }
        }


    }
}