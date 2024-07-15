package com.nophasenokill

import com.nophasenokill.extensions.configurePlugin
import com.nophasenokill.extensions.registerAndConfigureTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.utils.named

class JacocoPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            applyJacocoPlugin()

            val testTask = project.tasks.named<Test>("test")

            pluginManager.withPlugin("application") {

                val runTask = tasks.named<JavaExec>(ApplicationPlugin.TASK_RUN_NAME)

                configureJacocoExtension(runTask)
                addApplicationReportTask(runTask)
            }

            configureTestTask(testTask)
            ensureTestsRunBeforeGeneratingReport(testTask)
        }
    }


    private fun Project.applyJacocoPlugin() {
        pluginManager.apply("org.gradle.jacoco")
    }

    private fun Project.configureJacocoExtension(runTask: TaskProvider<out JavaExec>) {
        configurePlugin<JacocoPluginExtension> {
            applyTo(runTask.get())
        }
    }


    private fun Project.addApplicationReportTask(runTask: TaskProvider<out JavaExec>) {

        registerAndConfigureTask<JacocoReport>("applicationCodeCoverageReport") {
            val javaSourceSet = project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.getByName("main")

            executionData(runTask)
            sourceSets(javaSourceSet)
        }
    }


    private fun Project.configureTestTask(testTask: TaskProvider<Test>) {
        testTask.get().extensions.configure(JacocoTaskExtension::class.java) { extension: JacocoTaskExtension ->
            extension.destinationFile = layout.buildDirectory.file("jacoco/jacocoTest.exec").get().asFile
            /*
                Please note that this only gets put into the build dir the first time the task is run locally.
                It appears to then delete this/use other caching mechanisms, meaning that the next run,
                which deletes the destination file, will remove this dir.

                To ensure this is being generated, you will need to fully remove caches and try running
                buildAll again
             */
            extension.classDumpDir = layout.buildDirectory.dir("jacoco/classpathdumps").get().asFile
        }
    }

    private fun Project.ensureTestsRunBeforeGeneratingReport(testTask: TaskProvider<Test>) {
        tasks.withType(JacocoReport::class.java).configureEach { jacocoReportTask ->
            jacocoReportTask.dependsOn(testTask)
        }
    }
}





