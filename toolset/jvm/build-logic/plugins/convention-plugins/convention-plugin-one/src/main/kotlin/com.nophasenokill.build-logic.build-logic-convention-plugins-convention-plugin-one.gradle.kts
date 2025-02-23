import java.nio.file.Paths
import kotlin.io.path.pathString

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

group = "com.nophasenokill"
version = "1.0-local-dev"

dependencies {
    implementation(gradleApi())
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

val printRelativeProjectDirPath = tasks.register("printRelativeProjectDirPath") {
    val settingsFilePathOne = project.rootProject.projectDir.absolutePath
    val rootPath = Paths.get(settingsFilePathOne)
    val projectPath = Paths.get(project.projectDir.absolutePath)
    val relativeProjectPath = rootPath.relativize(projectPath)

    // Ensure the parent directory exists (the first task in the whole tree after a clean will need this)
    val parentDirectory = projectPath.resolve("build/custom-task/print-relative-project-dir-path").toFile()
    parentDirectory.mkdirs()

    val outputFile = parentDirectory.resolve("output-file.txt")

    if (!outputFile.exists()) {
        outputFile.createNewFile()
    }

    println("printRelativeProjectDirPath task for project: $project to: $outputFile")

    outputFile.writeText(relativeProjectPath.pathString)
}

tasks.test {
    dependsOn(printRelativeProjectDirPath)
    useJUnitPlatform()

    mustRunAfter(printRelativeProjectDirPath)
}

tasks.named("buildAll") {
    dependsOn(tasks.build)
}
