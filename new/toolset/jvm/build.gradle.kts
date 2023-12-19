import java.util.*


registerAllTask("build")
registerAllTask("clean")

val localTaskGroup = "Local"

/**
 * Reduces the noise in the exposed tasks, by simply doing the equivalent of a task at root (ie ./gradlew clean) behind
 * a scoped set of tasks in a 'group'.
 * registerAllTask("build") will register a task named 'buildAll',
 * that iterates over each of the subprojects and ensures :build is run on them.
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

        dependsOn(getTasksByName(taskName, true).map { it.path })
    }
}

tasks.named<TaskReportTask>("tasks") {
    displayGroup = localTaskGroup
}


