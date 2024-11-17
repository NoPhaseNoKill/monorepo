import java.util.*
import java.util.jar.JarFile
import java.util.zip.GZIPOutputStream
import java.security.MessageDigest

plugins {
    id("base")
    `java-gradle-plugin`
    `kotlin-dsl`
    `java-library`
    `maven-publish`

}

group = "org.sample"
version = "1.0"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        create("numberUtilsPlugin") {
            id = "org.sample.number-utils"
            implementationClass = "org.sample.NumberUtilsPlugin"
        }

        create("numberUtilsSettingsPlugin") {
            id = "org.sample.settings.number-utils"
            implementationClass = "org.sample.NumberUtilsSettingsPlugin"
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

val runtimeJar = tasks.register<Jar>("runtimeJar") {
    archiveClassifier.set("runtime")
    from(sourceSets.main.get().output)
}


val sharedConfiguration by configurations.creating {
    isCanBeResolved = false
}

artifacts {
    add(sharedConfiguration.name, runtimeJar)
}

publishing {
    repositories {
        maven {
            url = uri("${layout.buildDirectory.get()}/repo") // Local directory for troubleshooting/publishing
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

/*
    Task to easily publish the library locally which is primarily used to troubleshoot builds.

    Should only re-run the task only if sources are newer than the published JAR
 */

val publishToLocalRepo = tasks.register("publishToLocalRepo") {
    dependsOn(tasks.jar, tasks.classes)
    dependsOn("publishMavenJavaPublicationToMavenRepository")

    // Include both Java and Kotlin source files as inputs for incremental behavior
    inputs.files(fileTree("src/main/java"), fileTree("src/main/kotlin"))
    outputs.dir(layout.buildDirectory.dir("repo"))

    doLast {
        println("Publishing ${project.name} to local repository: ${layout.buildDirectory.dir("repo").get()} ")
    }

    mustRunAfter(tasks.named("jar"))
}

tasks.named("jar") {
    finalizedBy(publishToLocalRepo)
}

/*
    Prints the inputs for compile java/compile kotlin - so that we can once again troubleshoot builds.
 */

fun registerInputFileWriterTask(
    taskName: String,
    inputFiles: FileCollection
) {
    val titleCasedName = taskName.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
        else it.toString()
    }

    tasks.register<WriteInputFiles>("write${titleCasedName}InputFiles") {
        group = "Custom Tasks"
        description = "Writes input files for $taskName task to a file"
        this.inputFiles.from(inputFiles)
    }
}

project.pluginManager.withPlugin("java") {
    registerInputFileWriterTask("compileJava", tasks.named("compileJava").get().inputs.files)
    registerInputFileWriterTask("compileTestJava", tasks.named("compileTestJava").get().inputs.files)

    tasks.named("build") {
        dependsOn("writeCompileJavaInputFiles", "writeCompileTestJavaInputFiles")
    }
}

project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
    registerInputFileWriterTask("compileKotlin", tasks.named("compileKotlin").get().inputs.files)
    registerInputFileWriterTask("compileTestKotlin", tasks.named("compileTestKotlin").get().inputs.files)

    tasks.named("build") {
        dependsOn("writeCompileKotlinInputFiles", "writeCompileTestKotlinInputFiles")
    }
}

abstract class WriteInputFiles : DefaultTask() {

    @get:InputFiles
    abstract val inputFiles: ConfigurableFileCollection

    @get:OutputFile
    val outputFile = project.layout.buildDirectory.file("custom-tasks/input-files/${name}-input-files.txt")

    @TaskAction
    fun writeInputFiles() {
        val actualOutputFile = outputFile.get().asFile
        actualOutputFile.parentFile.mkdirs()
        actualOutputFile.writeText("") // Clear file before writing

        inputFiles.files.toSortedSet().forEach { file ->
            if (file.isFile) {
                actualOutputFile.appendText("Input file: ${file}\n")
            }
        }
        println("Input files for ${name} written to ${actualOutputFile}")
    }
}

tasks.register("writeBinaryAsHexToFile") {
    doLast {
        val binaryFile = file("/home/gardo/projects/samples/kotlin-stdlib-1_9_24_jar-snapshot.bin")
        val outputDir = file("${project.layout.buildDirectory.get()}/custom-tasks/write-binary-as-hex-to-file")
        outputDir.mkdirs()
        val outputFile = outputDir.resolve("hex_output.txt")
        outputFile.createNewFile()


        if (binaryFile.exists()) {
            println("Writing raw binary contents of ${binaryFile.absolutePath} to ${outputFile.absolutePath}")

            outputFile.printWriter().use { writer ->
                try {
                    binaryFile.inputStream().use { inputStream ->
                        val buffer = ByteArray(16)
                        var bytesRead: Int
                        var offset = 0
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            // Format each line with offset, hex bytes, and ASCII representation
                            val hexBytes = buffer.take(bytesRead).joinToString(" ") { "%02x".format(it) }
                            val asciiRepresentation = buffer.take(bytesRead).map {
                                if (it in 32..126) it.toChar() else '.'
                            }.joinToString("")
                            writer.println("%08x: %-47s | %s".format(offset, hexBytes, asciiRepresentation))
                            offset += bytesRead
                        }
                    }
                } catch (e: Exception) {
                    println("Failed to read binary file: ${e.message}")
                }
            }
            println("Hex output written to ${outputFile.absolutePath}")
        } else {
            println("File not found.")
        }
    }
}

tasks.register("compressHexOutput") {
    dependsOn("writeBinaryAsHexToFile") // Ensure the hex file is generated first

    doLast {
        val outputDir = file("${project.layout.buildDirectory.get()}/custom-tasks/write-binary-as-hex-to-file")
        val hexOutputFile = outputDir.resolve("hex_output.txt")
        val compressedFile = outputDir.resolve("hex_output.txt.gz")

        if (hexOutputFile.exists()) {
            println("Compressing ${hexOutputFile.absolutePath} to ${compressedFile.absolutePath}")

            compressedFile.outputStream().use { outputStream ->
                GZIPOutputStream(outputStream).use { gzipStream ->
                    hexOutputFile.inputStream().use { inputStream ->
                        inputStream.copyTo(gzipStream)
                    }
                }
            }
            println("Compression complete. Compressed file size: ${compressedFile.length()} bytes")
        } else {
            println("Hex output file not found. Please run 'writeBinaryAsHexToFile' first.")
        }
    }
}

dependencies {
    implementation("org.rocksdb:rocksdbjni:9.7.3")
}
