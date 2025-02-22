import java.nio.file.Files
import java.nio.file.Paths

plugins {
    id("base")
}

/*
    This prevents intellij from displaying subprojects incorrectly.

    For example, if you have the following project:

        rootProjectName.jvm

        jvm
        jvm/root-one/root-one-sub-project-one
        jvm/root-two/root-two-sub-project-one

    When using:
        include(":root-one-sub-project-one")
        include(":root-two-sub-project-one")

    Intellij by default displays in the UI the folders as:

        Folder:root-one/root-one-sub-project-one
        Displays as: root-one-sub-project-one[jvm.root-one-sub-project]

        Folder:root-two/root-two-sub-project-one
        Displays as: root-two-sub-project-one[jvm.root-two-sub-project]

    This fixes this, and instead displays it as follows:

        Folder:root-one/root-one-sub-project-one
        Displays as: root-one-sub-project-one

        Folder:root-two/root-two-sub-project-one
        Displays as: root-two-sub-project-one

    Notes:
        - This makes the assumption that if the user has already set the property,
        they are happy with displaying it incorrectly and we should NOT override it
        - It also makes the assumption that we may have multiple versions of intellij,
        and as such, does not discriminate - and applies this to all of them

 */

val fixIntellijIdeDisplayBugTask = tasks.register("fixIntellijIdeDisplayBug") {
    val baseDir = Paths.get(System.getProperty("user.home"), ".config", "JetBrains")
    val ideaPropertiesFiles = baseDir.toFile().walkTopDown().filter { it.name == "idea.properties" }.toList()

    inputs.files(ideaPropertiesFiles)
    outputs.files(ideaPropertiesFiles)

    doLast {
        ideaPropertiesFiles.forEach { file ->
            val fileContents = file.readText()
            val propertyAlreadyExists = fileContents.contains("ide.hide.real.module.name=")

            if (!propertyAlreadyExists) {
                println("Appending 'ide.hide.real.module.name=true' to ${file.absolutePath} to prevent issues from: https://youtrack.jetbrains.com/issue/IJPL-48535/Clean-module-names")
                file.appendText("\nide.hide.real.module.name=true")
            }
        }
    }

    // Ensure the task is only re-executed if something changes in the files
    outputs.upToDateWhen {
        val anyFileModified = ideaPropertiesFiles.any { path ->
            val lastModifiedTime = Files.getLastModifiedTime(path.toPath())
            val currentModifiedTime = Files.getLastModifiedTime(path.toPath())
            lastModifiedTime != currentModifiedTime
        }
        !anyFileModified // If no files are modified, the task is up-to-date
    }
}

tasks.build {
    dependsOn(fixIntellijIdeDisplayBugTask)
    dependsOn(gradle.includedBuilds.map { it.task(":build") })
}
