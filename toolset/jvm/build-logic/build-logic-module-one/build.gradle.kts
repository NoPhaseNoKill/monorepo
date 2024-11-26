import java.nio.file.Paths
import kotlin.io.path.pathString

plugins {
    kotlin("jvm") version ("2.0.20")
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
    val rootPathOne = Paths.get(settingsFilePathOne)
    val projectPathOne = Paths.get(project.projectDir.absolutePath)
    val relativeProjectPathOne = rootPathOne.relativize(projectPathOne)

    // Ensure the parent directory exists (the first task in the whole tree after a clean will need this)
    val parentDirectory = projectPathOne.resolve("build/custom-task/print-relative-project-dir-path").toFile()
    parentDirectory.mkdirs()

    val outputFile = parentDirectory.resolve("output-file.txt")

    if (!outputFile.exists()) {
        outputFile.createNewFile()
    }

    outputFile.writeText(relativeProjectPathOne.pathString)
}

tasks.test {
    dependsOn(printRelativeProjectDirPath)
    useJUnitPlatform()

    mustRunAfter(printRelativeProjectDirPath)
}

tasks.named("buildAll") {
    dependsOn(tasks.build)
}