import java.util.*

plugins {
    alias(libs.plugins.kotlinDsl) apply(false)
    alias(libs.plugins.kotlinJvm) apply(false)
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.composeCompiler) apply false
}

/*
    Reduces the noise in the exposed tasks, by simply doing the equivalent of a task at root (ie ./gradlew clean) behind
    a scoped set of tasks in a 'group'.
    registerAllTask("build") will register a task named 'buildAll',
    that iterates over each of the all included builds' subprojects + all subprojects :build is run on EACH them.

    Example:

    registerAllTask("build")
    registerAllTask("clean")

    Will output:

    Local tasks
    -----------
    buildAll - Build all included builds subprojects' + all subprojects builds
    cleanAll - Clean all included builds subprojects' + all subprojects builds

 */

registerAllTask("build")
registerAllTask("clean")
registerAllTask("test")
registerAllTask("run")
registerAllTask("check")

val localTaskGroup = "Local"

/**
 * Reduces the noise in the exposed tasks, by simply doing the equivalent of a task at root (ie ./gradlew clean) behind
 * a scoped set of tasks in a 'group'.
 * registerAllTask("build") will register a task named 'buildAll',
 * that iterates over each of the all included builds subprojects' + all subprojects, and ensure :build is run on EACH them.
 */
fun registerAllTask(
    taskName: String,
    additionalActions: Task.() -> Unit = {}
) {
    val taskDescription = taskName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    tasks.register("${taskName}All") {
        group = localTaskGroup
        description = "$taskDescription all included builds subprojects' + all subprojects"
        additionalActions()

        registerTaskEquivalents(this, taskName)
    }
}

fun registerTaskEquivalents(task: Task, taskName: String) {

    /*
        Dynamically includes any projects (something with a build.gradle.kts)
        and excludes the root project.

        This ensures that any included builds are also a part of running this task, so you have a single centralized place
        to run/build/test/check etc everything.

     */
    gradle.includedBuilds.forEach { includedBuild ->
        includedBuild.projectDir.walkTopDown().forEach { file ->
            if (file.isFile && file.name == "build.gradle.kts") {

                val relativePath = file.parentFile.relativeTo(includedBuild.projectDir).path.replace(File.separator, ":")
                val isNotRootPath = relativePath.isNotEmpty()

                if (isNotRootPath) {
                    // Equivalent of making buildAll task depend on : ':build-logic:kotlin-plugins:build' for example
                    task.dependsOn(includedBuild.task(":${relativePath}:${taskName}"))
                    logger.quiet("Dynamically included ${taskName} task: :${includedBuild.name}:${relativePath}:${taskName}")
                }
            }
        }
    }
}

tasks.named<TaskReportTask>("tasks") {
    displayGroup = localTaskGroup
}