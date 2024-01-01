import java.util.*

plugins {
    id("integraboost.plugin.root.dependency-analysis-plugin")
}

registerAllTask("build") {
    println("COMING INTO BUILD")
}
registerAllTask("clean") {
    println("COMING INTO CLEAN")
}

val localTaskGroup = "Local"

/**
 * Reduces the noise in the exposed tasks, by simply doing the equivalent of a task at root (ie ./gradlew clean) behind
 * a scoped set of tasks in a 'group'.
 * registerAllTask("build") will register a task named 'buildAll',
 * that iterates over each of the subprojects and composite builds and ensures :build is run on EACH them.
 */
fun registerAllTask(
    taskName: String,
    additionalActions: Task.() -> Unit = {}
) {
    val taskDescription = taskName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    tasks.register("${taskName}All") {
        group = localTaskGroup
        description = "$taskDescription all subprojects/composite builds"
        additionalActions()

        registerTaskEquivalents(this, taskName)
    }
}

fun registerTaskEquivalents(task: Task, taskName: String) {
    // Composite builds are not a part of the tasks like subprojects and need to be 'manually' handled
    task
        .dependsOn(gradle
                .includedBuild("build-logic")
                .task(":plugins:${taskName}")
                .also {
                    println("Included composite build task:  :build-logic:plugins:${taskName}")
                }
        )

    // subprojects
    task
        .dependsOn(
            getTasksByName(taskName, true)
                .map { subproject ->
                    println("Included subproject build task: ${subproject.path}")
                    subproject.path
                }
        )
}

tasks.named<TaskReportTask>("tasks") {
    displayGroup = localTaskGroup
}


