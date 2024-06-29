package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.version

class RootSettingsPlugin: Plugin<Settings> {
    override fun apply(settings: Settings) {
        settings.run {
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
        }
    }
}