package com.nophasenokill.app

import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.file.Paths


fun main() {
    runWarmedTestSuite()
}

fun runWarmedTestSuite() {
    val currentPath = Paths.get("").toAbsolutePath().toString()
    val projectDir = File(currentPath)

    val connector = GradleConnector.newConnector().forProjectDirectory(projectDir)
    connector.connect().use { connection: ProjectConnection ->
        runTestsUltraFast(connection) // this will warm up the tests as I don't yet want to implement keeping it open without having a way to close connection easily
        runTestsUltraFast(connection) // this will produce the actual test of speed i'm looking for

        // Task executed in 1580 ms
        // Build Output:
        // Reusing configuration cache.
        // > Task :modules:applications:app:processResources SKIPPED
        // > Task :modules:libraries:list:processResources SKIPPED
        // > Task :modules:libraries:utilities:processResources SKIPPED
        // > Task :modules:libraries:list:checkKotlinGradlePluginConfigurationErrors
        // > Task :modules:libraries:utilities:checkKotlinGradlePluginConfigurationErrors
        // > Task :modules:applications:app:checkKotlinGradlePluginConfigurationErrors
        // > Task :build-logic:plugins-new:plugin:processTestResources NO-SOURCE
        // > Task :build-logic:plugins-new:plugin:pluginDescriptors UP-TO-DATE
        // > Task :build-logic:plugins-new:plugin:compileJava NO-SOURCE
        // > Task :modules:applications:app:processTestResources UP-TO-DATE
        // > Task :modules:libraries:list:processTestResources UP-TO-DATE
        // > Task :modules:libraries:utilities:processTestResources UP-TO-DATE
        // > Task :build-logic:plugins-new:plugin:processResources UP-TO-DATE
        // > Task :build-logic:plugins-new:plugin:classes UP-TO-DATE
        // > Task :build-logic:plugins-new:plugin:compileTestJava NO-SOURCE
        // > Task :build-logic:plugins-new:plugin:testClasses UP-TO-DATE
        // > Task :modules:libraries:list:compileKotlin UP-TO-DATE
        // > Task :build-logic:plugins-new:plugin:pluginUnderTestMetadata UP-TO-DATE
        // > Task :modules:libraries:list:compileJava SKIPPED
        // > Task :modules:libraries:list:classes UP-TO-DATE
        // > Task :modules:libraries:list:jar UP-TO-DATE
        // > Task :modules:libraries:list:compileTestKotlin UP-TO-DATE
        // > Task :modules:libraries:utilities:compileKotlin UP-TO-DATE
        // > Task :modules:libraries:list:compileTestJava SKIPPED
        // > Task :modules:libraries:utilities:compileJava SKIPPED
        // > Task :modules:libraries:list:testClasses UP-TO-DATE
        // > Task :modules:libraries:utilities:classes UP-TO-DATE
        // > Task :modules:libraries:utilities:jar UP-TO-DATE
        // > Task :build-logic:plugins-new:plugin:test NO-SOURCE
        // > Task :modules:libraries:list:test UP-TO-DATE
        // > Task :modules:libraries:utilities:compileTestKotlin UP-TO-DATE
        // > Task :modules:applications:app:compileKotlin UP-TO-DATE
        // > Task :modules:libraries:utilities:compileTestJava SKIPPED
        // > Task :modules:libraries:utilities:testClasses UP-TO-DATE
        // > Task :modules:applications:app:compileJava SKIPPED
        // > Task :modules:applications:app:classes UP-TO-DATE
        // > Task :modules:libraries:utilities:test UP-TO-DATE
        // > Task :modules:applications:app:compileTestKotlin UP-TO-DATE
        // > Task :modules:applications:app:compileTestJava SKIPPED
        // > Task :modules:applications:app:testClasses UP-TO-DATE
        // > Task :modules:applications:app:test UP-TO-DATE
        //
        // BUILD SUCCESSFUL in 701ms
        // 20 actionable tasks: 3 executed, 17 up-to-date
        //
        // Publishing build scan...
        // https://gradle.com/s/ssowmpuvs726u
        //
        // Configuration cache entry reused.
        //
        // Task executed in 1426 ms
        // Build Output:
        // Reusing configuration cache.
        // > Task :modules:applications:app:processResources SKIPPED
        // > Task :modules:libraries:list:processResources SKIPPED
        // > Task :modules:libraries:utilities:processResources SKIPPED
        // > Task :build-logic:plugins-new:plugin:pluginDescriptors UP-TO-DATE
        // > Task :modules:libraries:list:checkKotlinGradlePluginConfigurationErrors
        // > Task :build-logic:plugins-new:plugin:processTestResources NO-SOURCE
        // > Task :modules:applications:app:processTestResources UP-TO-DATE
        // > Task :modules:libraries:list:processTestResources UP-TO-DATE
        // > Task :modules:libraries:utilities:processTestResources UP-TO-DATE
        // > Task :build-logic:plugins-new:plugin:compileJava NO-SOURCE
        // > Task :build-logic:plugins-new:plugin:processResources UP-TO-DATE
        // > Task :build-logic:plugins-new:plugin:classes UP-TO-DATE
        // > Task :modules:libraries:utilities:checkKotlinGradlePluginConfigurationErrors
        // > Task :modules:applications:app:checkKotlinGradlePluginConfigurationErrors
        // > Task :build-logic:plugins-new:plugin:pluginUnderTestMetadata UP-TO-DATE
        // > Task :build-logic:plugins-new:plugin:compileTestJava NO-SOURCE
        // > Task :build-logic:plugins-new:plugin:testClasses UP-TO-DATE
        // > Task :modules:libraries:list:compileKotlin UP-TO-DATE
        // > Task :modules:libraries:list:compileJava SKIPPED
        // > Task :modules:libraries:list:classes UP-TO-DATE
        // > Task :modules:libraries:list:jar UP-TO-DATE
        // > Task :modules:libraries:utilities:compileKotlin UP-TO-DATE
        // > Task :modules:libraries:utilities:compileJava SKIPPED
        // > Task :modules:libraries:utilities:classes UP-TO-DATE
        // > Task :modules:libraries:list:compileTestKotlin UP-TO-DATE
        // > Task :modules:libraries:list:compileTestJava SKIPPED
        // > Task :modules:libraries:utilities:jar UP-TO-DATE
        // > Task :modules:libraries:list:testClasses UP-TO-DATE
        // > Task :modules:libraries:utilities:compileTestKotlin UP-TO-DATE
        // > Task :modules:applications:app:compileKotlin UP-TO-DATE
        // > Task :build-logic:plugins-new:plugin:test NO-SOURCE
        // > Task :modules:applications:app:compileJava SKIPPED
        // > Task :modules:libraries:utilities:compileTestJava SKIPPED
        // > Task :modules:applications:app:classes UP-TO-DATE
        // > Task :modules:libraries:utilities:testClasses UP-TO-DATE
        // > Task :modules:libraries:list:test UP-TO-DATE
        // > Task :modules:libraries:utilities:test UP-TO-DATE
        // > Task :modules:applications:app:compileTestKotlin UP-TO-DATE
        // > Task :modules:applications:app:compileTestJava SKIPPED
        // > Task :modules:applications:app:testClasses UP-TO-DATE
        // > Task :modules:applications:app:test UP-TO-DATE
        //
        // BUILD SUCCESSFUL in 59ms
        // 20 actionable tasks: 3 executed, 17 up-to-date
        //
        // Publishing build scan...
        // https://gradle.com/s/mu6yuuef5xdrs
        //
        // Configuration cache entry reused.
    }
}

private fun runTestsUltraFast(connection: ProjectConnection) {
    val startTime = System.currentTimeMillis()

    val outputStream = ByteArrayOutputStream()

    connection
        .newBuild()
        .forTasks("test")
        .withArguments("--console=plain")
        .setStandardOutput(PrintStream(outputStream))
        .run()

    // Measure end time and calculate the duration
    val endTime = System.currentTimeMillis()
    val duration = (endTime - startTime)

    val output = outputStream.toString()
    println("Task executed in $duration ms")
    println("Build Output:\n$output")
}
