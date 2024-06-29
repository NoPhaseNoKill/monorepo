package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.version

class RootSettingsPlugin: Plugin<Settings> {
    override fun apply(settings: Settings) {
        settings.run {
            /*
                Due to loading of plugins, if this is ever updated, the build-service-warning-fix-plugin
                will need to be updated also.
             */
            pluginManagement {
                plugins {
                    id("org.jetbrains.kotlin.jvm") version("1.9.22")
                }
                repositories.mavenCentral()
                repositories.gradlePluginPortal()
            }

            dependencyResolutionManagement {
                repositories.mavenCentral()
                repositories.gradlePluginPortal()
            }

            enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
            enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
        }
    }
}