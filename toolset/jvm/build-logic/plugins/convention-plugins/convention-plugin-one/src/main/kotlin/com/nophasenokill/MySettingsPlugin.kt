package com.nophasenokill

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create
import java.io.File

abstract class MySettingsExtension(private val settings: Settings) {
    abstract val applyCustomLogic: Property<Boolean>
    val LOGGER = Logging.getLogger(Settings::class.java)

    fun includeCustomProject(projectName: String, type: ProjectType) {
        val projectNamePrefix = type.path.replace("${File.separatorChar}", ":")

        LOGGER.quiet("")
        val boldedDetailsHeading = "\u001B[1mTroubleshooting details\u001B[0m"
        LOGGER.quiet("""
        *** Including project final result: ':${projectNamePrefix}:$projectName' ***
            ${boldedDetailsHeading}
                [project-name-original]     '${projectName}'
                [project-type]              '${type.path}'
                [project-name-prefix]       '${projectNamePrefix}'
                [project-path]              '${type.path}/${projectName}'
                [included-name-aka-final]   ':${projectNamePrefix}:$projectName'
            """.trimIndent()
        )

        settings.include(":$projectName")
        settings.project(":$projectName").projectDir = File("${type.path}/${projectName}")
    }
}


abstract class MySettingsPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        val extension = settings.extensions.create<MySettingsExtension>("mySettings", settings).apply {
            applyCustomLogic.convention(false)
        }

        settings.gradle.settingsEvaluated {
            if (extension.applyCustomLogic.get()) {
                println("Custom logic has been applied!")
            } else {
                println("Custom logic was skipped!")
            }
        }
    }
}



fun Settings.mySettings(action: Action<MySettingsExtension>) {
    (this as ExtensionAware).extensions.configure("mySettings", action)
}
