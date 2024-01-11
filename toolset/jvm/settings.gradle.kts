import com.google.gson.GsonBuilder

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    includeBuild("build-logic/settings")
    includeBuild("build-logic/plugins")
}


dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    includeBuild("build-logic/platforms")
}

plugins {
    id("kotlin-project-root-settings")
}

rootProject.name = "jvm"

// Dynamically includes top level directories within each of the modules' sub-folders
val directories = setOf("applications", "libraries")
directories.forEach { dir ->
    rootDir
        .resolve("modules/${dir}")
        .listFiles { file -> file.isDirectory && !file.isHidden }
        ?.forEach {
            include("modules:$dir:${it.name}")
    }
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

buildCache {
    local {
        isEnabled = true
        isPush = true
    }
}

data class TaskSnapshot(
    val projectName: String,
    val taskName: String,
    val taskInputs: List<TaskSnapshotInput>,
    val taskOutputs: List<TaskSnapshotOutput>,
) {
    override fun toString(): String {
        return """
            {
              "project_name": "${this.projectName}",
              "task_name": "${this.taskName}",
              "task_inputs": ${taskInputs.joinToString(",\n", "[\n", "\n]") { "{ \"name\": \"${it.name}\", \"value\": \"${it.value}\" }"  } },
              "task_outputs": ${taskOutputs.joinToString(",\n", "[\n", "\n]") { "{ \"name\": \"${it.name}\", \"path\": \"${it.path}\" }"  } }
            }
        """.trimIndent()
    }
}

data class TaskSnapshotInput(
    val name: String,
    val value: Any?,
)

data class TaskSnapshotOutput(
    val name: String,
    val path: String,
)

gradle.taskGraph.whenReady {
    val all = this.allTasks
    val taskSnapshots = all.map { task: Task ->
        val taskInputs = task.inputs.properties.entries.map {
            TaskSnapshotInput(it.key, it.value)
        }

        val taskOutputs: List<TaskSnapshotOutput> = task.outputs.files.asFileTree.files.map { file ->
            TaskSnapshotOutput(file.name, file.path)
        }.sortedByDescending { it.name } // As per docs: "The order of the files in a FileTree is not stable"

        TaskSnapshot(
            task.project.name,
            task.name,
            taskInputs,
            taskOutputs
        )

    }

    val unformatted = File("${rootDir}/snapshots/unformatted-tasks.json")
    val formatted = File("${rootDir}/snapshots/formatted-tasks.json")

    unformatted.bufferedWriter().use { writer ->
        writer.append("{ \"tasks\": [ ")
        taskSnapshots.forEachIndexed { index, taskSnapshot ->
            writer.append(taskSnapshot.toString())
            if (index != taskSnapshots.size - 1) {
                writer.append(",")
            }
        }


        writer.append("\n]\n}")
    }.close()

    val gson = GsonBuilder().setPrettyPrinting().create()
    val jsonContent = unformatted.readText()
    val jsonElement = gson.fromJson(jsonContent, Any::class.java)
    val formattedJson = gson.toJson(jsonElement)
    formatted.writeText(formattedJson)
    unformatted.delete()
}