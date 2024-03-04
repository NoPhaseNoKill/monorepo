package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.kotlin.dsl.dependencies


class CommonsTestedPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.plugins.apply(ApplicationPlugin::class.java)
        project.plugins.apply("org.jetbrains.kotlin.jvm")
        project.plugins.apply("java")

        // // // configure details of all '*checkKotlinGradlePluginConfigurationErrors*' tasks that are part of the task graph
        // project.tasks.matching { it.name.contains("checkKotlinGradlePluginConfigurationErrors") }.configureEach {
        //     val output: String =
        //         if (state.failure != null) {
        //             "Configuration error detected: ${state.failure?.message}"
        //         } else {
        //             "Check Kotlin Gradle Plugin Configuration Errors task was successful."
        //         }
        //
        //     // assumed up to date unless kotlin build folders change, signifying plugin needs to be re-checked
        //     inputs.dir(project.layout.buildDirectory.dir("classes"))
        //     inputs.dir(project.layout.buildDirectory.dir("kotlin"))
        //
        //     val outputDir = project.layout.buildDirectory.dir("extendedCheckKotlinGradlePluginConfigurationErrors")
        //     val outputFile = outputDir.map {
        //         it.file("extendedCheckKotlinGradlePluginConfigurationErrorsResult.txt")
        //     }
        //     outputs.file(outputFile)
        //
        //     doLast {
        //         outputFile.get().asFile.writeText("") // clear file contents
        //         outputFile.get().asFile.appendText(output)
        //     }
        // }

        //TODO WIP FINISH THIS OFF
        // project.configurations.named("testImplementation") {
        //     dependencies.addAllLater(project.provider {
        //         project.dependencies.run {
        //             listOf(
        //                 // https://junit.org/junit5/docs/current/user-guide/#running-tests-build-gradle-bom
        //                 platform("org.junit:junit-bom:${config.test.junit}"),
        //                 create("org.junit.jupiter:junit-jupiter"),
        //             )
        //         }
        //     })
        // }




        project.dependencies {
            add("implementation", "org.jetbrains.kotlin:kotlin-stdlib:1.9.20" )
        }


        // Register task lazily instead of eagerly
        project.tasks.register("taskInsideCommonsTestedPlugin") {
            this.doLast {
                logger.lifecycle("Hello from plugin 'commons-tested-plugin'")
            }
        }
    }
}
