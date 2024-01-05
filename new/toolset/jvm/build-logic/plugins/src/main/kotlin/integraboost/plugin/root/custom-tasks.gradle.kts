package integraboost.plugin.root

tasks.register("buildAll") {
    group = "local"
    description = "Build all subprojects"
    subprojects.forEach { subproject ->
        if (subproject.tasks.findByName("build") != null) {
            dependsOn(subproject.tasks.named("build"))
        }
    }

    finalizedBy(tasks.named("outputTaskDependenciesToFile"))
}

tasks.register("cleanAll") {
    group = "local"
    description = "Clean all subprojects"
    subprojects.forEach { subproject ->
        if (subproject.tasks.findByName("clean") != null) {
            dependsOn(subproject.tasks.named("clean"))
        }
    }

    finalizedBy(tasks.named("outputTaskDependenciesToFile"))
}

tasks.register("outputTaskDependenciesToFile") {

    group = "profiler"
    description = "Output all task dependency trees to file"

    val taskProfilerDirectory = layout.projectDirectory.dir("profiler/tasks")

    doLast {

        val cleanAllOriginal = taskProfilerDirectory.dir("cleanAll").file("previous-task-tree.txt").asFile
        val buildAllOriginal = taskProfilerDirectory.dir("buildAll").file("previous-task-tree.txt").asFile

        val cleanAllFile = taskProfilerDirectory.dir("cleanAll").file("new-task-tree.txt").asFile
        val buildAllFile = taskProfilerDirectory.dir("buildAll").file("new-task-tree.txt").asFile

        cleanAllFile.createNewFile()
        buildAllFile.createNewFile()

        val buildAllTask = tasks.named("buildAll").get()
        val cleanAllTask = tasks.named("cleanAll").get()

        writeTaskDependenciesToFile(buildAllTask, buildAllFile)
        writeTaskDependenciesToFile(cleanAllTask, cleanAllFile)

        val isCleanAllSame = trimEmptyLinesFromBytes(cleanAllFile.readBytes()).contentEquals(trimEmptyLinesFromBytes(cleanAllOriginal.readBytes()))
        val isBuildAllSame = trimEmptyLinesFromBytes(buildAllFile.readBytes()).contentEquals(trimEmptyLinesFromBytes(buildAllOriginal.readBytes()))

        if(!isCleanAllSame || !isBuildAllSame) {
            throw Exception("Tasks have changed. isCleanAllSame: ${isCleanAllSame}, isBuildAllSame: ${isBuildAllSame}. Please fix the file if this was intentional. Refer to new-task-tree.txt files for differences")
        } else {
            // used to avoid unnecessary caching validation
            copy {
                delete(cleanAllFile)
                delete(buildAllFile)

                from(cleanAllFile)
                into(cleanAllOriginal)

                from(buildAllFile)
                into(buildAllOriginal)
            }
        }
    }
}

// recursively writes all dependencies to file
fun writeTaskDependenciesToFile(task: Task, file: File) {
    task.taskDependencies.getDependencies(task).sorted().forEach { taskDependency ->
        file.appendText(taskDependency.path + "\n")
        writeTaskDependenciesToFile(taskDependency, file)
    }
}

fun trimEmptyLinesFromBytes(bytes: ByteArray): ByteArray {
    val content = String(bytes)

    val trimmedContent = content.lines()
        .filter { it.isNotBlank() }
        .joinToString(separator = "\n")

    return trimmedContent.toByteArray()
}