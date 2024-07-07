import java.util.*

plugins {
    alias(libs.plugins.kotlinJvm) apply(false)
}

/*
    Reduces the noise in the exposed tasks, by simply doing the equivalent of a task at root (ie ./gradlew clean) behind
    a scoped set of tasks in a 'group'.
    registerAllTask("build") will register a task named 'buildAll',
    that iterates over each of the subprojects :build is run on EACH them.

    Example:

    registerAllTask("build")
    registerAllTask("clean")

    Will output:

    Local tasks
    -----------
    buildAll - Build all subprojects builds
    cleanAll - Clean all subprojects builds

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
 * that iterates over each of the subprojects, and ensure :build is run on EACH them.
 */
fun registerAllTask(
    taskName: String,
    additionalActions: Task.() -> Unit = {}
) {
    val taskDescription = taskName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    tasks.register("${taskName}All") {
        group = localTaskGroup
        description = "$taskDescription all subprojects"
        additionalActions()

        registerTaskEquivalents(this, taskName)
    }
}

fun registerTaskEquivalents(task: Task, taskName: String) {

    /*
        Dynamically includes any projects (something with a build.gradle.kts)
        and excludes the root project.
     */
    rootDir.walk().forEach { file ->
        if (file.isFile && file.name == "build.gradle.kts") {
            val parentFileRelativeToRootDir = file.parentFile.relativeTo(rootDir)
            val replacedRelativePath = parentFileRelativeToRootDir.path.replace(File.separator, ":")

            if(parentFileRelativeToRootDir.path.isNotEmpty() && replacedRelativePath !== "") {
                task
                    .dependsOn(
                        getTasksByName(taskName, true)
                            .map { subproject ->
                                logger.debug("Included subproject build task: ${subproject.path}")
                                subproject.path
                            }
                    )
            }
        }
    }


}

tasks.named<TaskReportTask>("tasks") {
    displayGroup = localTaskGroup
}