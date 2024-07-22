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
            // applyJacocoPlugin()

            // val testTask = project.tasks.named<Test>("test")
            //
            // pluginManager.withPlugin("application") {
            //
            //     val runTask = tasks.named<JavaExec>(ApplicationPlugin.TASK_RUN_NAME)
            //
            //     configureJacocoExtension(runTask)
            //     addApplicationReportTask(runTask)
            // }
            //
            // configureTestTask(testTask)
            // ensureTestsRunBeforeGeneratingReport(testTask)
            //
            // val jacocoTestReport = tasks.named<JacocoReport>("jacocoTestReport")
            //
            // configureJacocoTestReportTask(jacocoTestReport)
            // registerSessionsTask(jacocoTestReport)
            // registerComparisonTask()
        }
    }


    // private fun Project.applyJacocoPlugin() {
    //     pluginManager.apply("org.gradle.jacoco")
    // }
    //
    // private fun Project.configureJacocoExtension(runTask: TaskProvider<out JavaExec>) {
    //     configurePlugin<JacocoPluginExtension> {
    //         applyTo(runTask.get())
    //     }
    // }
    //
    //
    // private fun Project.addApplicationReportTask(runTask: TaskProvider<out JavaExec>) {
    //
    //     registerAndConfigureTask<JacocoReport>("applicationCodeCoverageReport") {
    //         val javaSourceSet = project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.getByName("main")
    //
    //         executionData(runTask)
    //         sourceSets(javaSourceSet)
    //     }
    // }
    //
    //
    // private fun Project.configureTestTask(testTask: TaskProvider<Test>) {
    //     testTask.get().extensions.configure(JacocoTaskExtension::class.java) { extension: JacocoTaskExtension ->
    //         extension.destinationFile = layout.buildDirectory.file("jacoco/jacocoTest.exec").get().asFile
    //         /*
    //             Please note that this only gets put into the build dir the first time the task is run locally.
    //             It appears to then delete this/use other caching mechanisms, meaning that the next run,
    //             which deletes the destination file, will remove this dir.
    //
    //             To ensure this is being generated, you will need to fully remove caches and try running
    //             buildAll again
    //          */
    //         extension.classDumpDir = layout.buildDirectory.dir("jacoco/classpathdumps").get().asFile
    //     }
    // }
    //
    // private fun Project.ensureTestsRunBeforeGeneratingReport(testTask: TaskProvider<Test>) {
    //     tasks.withType(JacocoReport::class.java).configureEach { jacocoReportTask ->
    //         jacocoReportTask.dependsOn(testTask)
    //     }
    // }
    //
    // private fun Project.configureJacocoTestReportTask(jacocoTestReport: TaskProvider<JacocoReport>) {
    //
    //     jacocoTestReport.get().reports.html.outputLocation.set(layout.buildDirectory.dir("jacocoHtmlOutputs"))
    //     jacocoTestReport.get().reports.xml.required.set(false)
    //     jacocoTestReport.get().reports.csv.required.set(false)
    // }
    //
    // private fun Project.registerComparisonTask() {
    //     tasks.register("compareSessions") {
    //         it.dependsOn(tasks.named("sessions"))
    //
    //         val currentSessionFile = layout.buildDirectory.file("jacoco-session/jacoco-sessions.txt")
    //         val previousSessionFile = layout.buildDirectory.file("jacoco-session/previous-jacoco-sessions.txt")
    //         val shouldRunTests = layout.buildDirectory.file("jacoco-session/should-run-tests.txt")
    //         val difference = layout.buildDirectory.file("jacoco-session/difference.txt")
    //
    //         it.inputs.file(tasks.named("sessions").get().outputs.files.first())
    //         it.outputs.files(previousSessionFile, shouldRunTests, difference)
    //
    //         it.doLast {
    //             val currentContentLines = if (currentSessionFile.get().asFile.exists()) {
    //                 currentSessionFile.get().asFile.readLines()
    //             } else {
    //                 logger.warn("Current session file not found.")
    //                 shouldRunTests.get().asFile.createNewFile()
    //                 shouldRunTests.get().asFile.writeText("true")
    //                 return@doLast
    //             }
    //
    //             val previousContentLines = if (previousSessionFile.get().asFile.exists()) {
    //                 previousSessionFile.get().asFile.readLines()
    //             } else {
    //                 // No previous data to compare with, assume first run
    //                 logger.info("No previous session file found. Saving current as previous for future comparisons.")
    //                 previousSessionFile.get().asFile.writeText(currentContentLines.joinToString("\n"))
    //                 shouldRunTests.get().asFile.createNewFile()
    //                 shouldRunTests.get().asFile.writeText("true")
    //                 return@doLast
    //             }
    //
    //             // Detect and print line differences
    //             val differences = currentContentLines.zip(previousContentLines).filterNot { (current, previous) -> current == previous }
    //             if (differences.isEmpty() && currentContentLines.size == previousContentLines.size) {
    //                 logger.lifecycle("No changes detected between sessions.")
    //                 shouldRunTests.get().asFile.createNewFile()
    //                 shouldRunTests.get().asFile.writeText("false")
    //             } else {
    //                 logger.lifecycle("Changes detected between sessions!")
    //                 difference.map { it.asFile.createNewFile() }
    //                 differences.forEach { (current, previous) ->
    //                     difference.get().asFile.appendText("Previous: $previous -> Current: $current")
    //                     logger.lifecycle("Previous: $previous -> Current: $current")
    //                 }
    //
    //                 // Check for extra lines in current file
    //                 if (currentContentLines.size > previousContentLines.size) {
    //                     currentContentLines.subList(previousContentLines.size, currentContentLines.size).forEach {
    //                         difference.get().asFile.appendText("Previous: null -> Current: $it")
    //                         logger.lifecycle("New line: $it")
    //                     }
    //                 }
    //
    //                 shouldRunTests.get().asFile.createNewFile()
    //                 shouldRunTests.get().asFile.writeText("true")
    //             }
    //
    //             // Update the previous file with current for next comparison
    //             previousSessionFile.get().asFile.writeText(currentContentLines.joinToString("\n"))
    //         }
    //     }
    // }
    //
    // private fun Project.registerSessionsTask(jacocoTestReport: TaskProvider<JacocoReport>) {
    //     tasks.register("sessions") {
    //         it.mustRunAfter(jacocoTestReport)
    //         it.dependsOn(jacocoTestReport)
    //
    //         val inputDir = jacocoTestReport.get().outputs.files.asFileTree
    //         val outputDir = layout.buildDirectory.dir("jacoco-session")
    //         val newFile = outputDir.map { it.asFile.resolve("jacoco-sessions.txt") }
    //
    //         it.inputs.files(inputDir)
    //         it.outputs.file(newFile)
    //
    //         it.doLast {
    //             outputDir.get().asFile.mkdirs()
    //
    //             val sessionFile = inputDir.files.firstOrNull { it.name == "jacoco-sessions.html" }
    //             if (sessionFile != null && sessionFile.exists()) {
    //
    //                 newFile.get().createNewFile()
    //                 val readContents = extractClassData(sessionFile.readText())
    //                 val allContents = readContents.map { it.toString() }
    //                 val allContent = allContents.joinToString(separator = "\n") { content -> content }
    //                 newFile.get().writeText(allContent) // Writes all content at once, overwriting any existing content
    //
    //             } else {
    //                 logger.info("Jacoco sessions file not found, skipping 'sessions' task.")
    //             }
    //         }
    //     }
    // }
    //
    // private fun extractClassData(html: String): Map<String, String> {
    //     val result = mutableMapOf<String, String>()
    //     val rows = html.split("<tr>").drop(1)
    //
    //     for (row in rows) {
    //
    //         val classStart = row.indexOf(">") + 1
    //         val classEnd = row.indexOf("</")
    //         if (classStart == 0 || classEnd == -1) continue
    //         val classNamePart = row.substring(classStart, classEnd)
    //         val className = classNamePart.split(">").last().split("<").first()
    //
    //
    //         val idStart = row.indexOf("<code>") + 6
    //         val idEnd = row.indexOf("</code>")
    //         if (idStart < 6 || idEnd == -1) continue
    //         val classId = row.substring(idStart, idEnd)
    //
    //         if (className.isNotEmpty() && classId.isNotEmpty()) {
    //             result[className] = classId
    //         }
    //     }
    //     return result
    // }
}







