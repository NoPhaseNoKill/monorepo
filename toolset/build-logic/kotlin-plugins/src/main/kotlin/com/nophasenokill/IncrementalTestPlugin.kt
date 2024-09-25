package com.nophasenokill

import com.nophasenokill.extensions.registerAndConfigureTask
import com.nophasenokill.tasks.IncrementalTestTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class IncrementalTestPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        // project.pluginManager.apply("jacoco")

        project.pluginManager.withPlugin("java") {

            val callback =  { incrementalTask: IncrementalTestTask ->
                val testSourceDir = project.layout.projectDirectory.dir("src/test/kotlin")
                val testResultsDir = project.layout.buildDirectory.dir("test-results/incremental")

                // Ensure the test source directory exists or create it
                if (!testSourceDir.asFile.exists()) {
                    incrementalTask.logger.lifecycle("Creating missing test source directory for project: ${project.name}")
                    testSourceDir.asFile.mkdirs()
                }

                incrementalTask.testSrcDir.set(testSourceDir)
                incrementalTask.testResultsDir.set(testResultsDir)
            }


            // val callback2 = { incrementalTask: IncrementalTestTask ->
            //
            //     incrementalTask.dependsOn(project.tasks.withType(Test::class.java))
            //
            //     val testSourceDir = project.layout.projectDirectory.dir("src/test/kotlin")
            //     val testResultsDir = project.layout.buildDirectory.dir("test-results/incremental")
            //
            //     // Only set the test source directory if it exists
            //     if (testSourceDir.asFile.exists()) {
            //         incrementalTask.testSrcDir?.set(testSourceDir)
            //     } else {
            //         incrementalTask.logger.lifecycle("Test source directory does not exist for project: ${project.name}")
            //     }
            //
            //     incrementalTask.testResultsDir.set(testResultsDir)
            // }

            val incrementalTestTask = project.registerAndConfigureTask<IncrementalTestTask>("incrementalTest", callback)
            project.tasks.named("check").get().dependsOn(incrementalTestTask)
        }
    }
}
