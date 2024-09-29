package com.nophasenokill

import com.nophasenokill.tasks.IncrementalTestTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class IncrementalTestPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        project.pluginManager.withPlugin("java") {

            project.run {
                val incrementalTest = project.tasks.register<IncrementalTestTask>("incrementalTest")

                project.tasks.named("check").get().dependsOn(incrementalTest)
            }
        }
    }
}
