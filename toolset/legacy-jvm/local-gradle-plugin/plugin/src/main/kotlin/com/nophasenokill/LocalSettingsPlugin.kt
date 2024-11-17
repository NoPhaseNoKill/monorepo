package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class LocalSettingsPlugin: Plugin<Settings> {
    override fun apply(settings: Settings) {
        println("Applying LocalSettingsPlugin")
    }
}
