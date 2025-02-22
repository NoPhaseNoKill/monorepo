package com.nophasenokill

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create
import java.io.File

abstract class SafeGradleExtension(private val settings: Settings) {
    abstract val applyCustomLogic: Property<Boolean>
    val LOGGER = Logging.getLogger(Settings::class.java)

    fun includeCustomProject(projectName: String, type: ProjectType, pathFromCurrentDir: String) {
        val projectNamePrefix = type.pathFromRoot.replace("${File.separatorChar}", ":")
        val projectPathRelativeToDeclaration = if(pathFromCurrentDir == "") projectName else "${type.pathFromRoot}/${projectName}"
        val projectPathRelativeToRoot = if(pathFromCurrentDir == "") "$projectNamePrefix/$projectName" else "${type.pathFromRoot}/${projectName}"


        LOGGER.quiet("")
        val boldedDetailsHeading = "\u001B[1mTroubleshooting details\u001B[0m"
        LOGGER.quiet("""
        *** Including project final result: ':${projectNamePrefix}:$projectName' ***
            ${boldedDetailsHeading}
                [project-name-original]                             '${projectName}'
                [project-type]                                      '${type}'
                [project-name-prefix]                               '${projectNamePrefix}'
                [project-path-relative-to-declaration]              '${projectPathRelativeToDeclaration}'
                [project-path-relative-to-root]                     '${projectPathRelativeToRoot}'
                [included-name-aka-final]                           ':${projectNamePrefix}:$projectName'
            """.trimIndent()
        )

        settings.include(":$projectName")
        settings.project(":$projectName").projectDir = File(projectPathRelativeToDeclaration)
    }
}


abstract class SafeGradlePlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        val extension = settings.extensions.create<SafeGradleExtension>("safeGradleExtension", settings).apply {
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



fun Settings.safeGradleExtension(action: Action<SafeGradleExtension>) {
    (this as ExtensionAware).extensions.configure("safeGradleExtension", action)
}
