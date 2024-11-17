package com.nophasenokill.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileType
import org.gradle.api.tasks.*
import org.gradle.work.ChangeType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

@CacheableTask
abstract class IncrementallyHashBytecodeFilesToMD5Task : DefaultTask() {

    @get:Incremental
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val inputDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    private fun processFile(sourceFile: File, targetFile: File) {
        val md = MessageDigest.getInstance("MD5")
        val fileBytes = sourceFile.readBytes()

        // Compute the file hash
        val fileDigest = md.digest(fileBytes)
        val fileHash = BigInteger(1, fileDigest).toString(16).padStart(32, '0')
        val hexRepresentation = fileBytes.joinToString("") { "%02x".format(it) }

        targetFile.parentFile.mkdirs() // Ensure output directory exists
        targetFile.writeText("inputFile: ${sourceFile.name}\n\n")
        targetFile.appendText("hashcode: $fileHash\n\n")
        targetFile.appendText("Hexadecimal representation of file contents: $hexRepresentation\n\n")

        println("Generated hash report for file: ${sourceFile.name} at ${targetFile.absolutePath}")
    }

    @TaskAction
    fun perform(inputChanges: InputChanges) {
        println(if (inputChanges.isIncremental) "Executing incrementally" else "Executing non-incrementally")

        inputChanges.getFileChanges(inputDir).forEach { change ->

            // Exclude synthetic inner classes (those containing $)
            val isSyntheticClass = change.file.name.contains("$")
            val isDirectory = change.fileType == FileType.DIRECTORY

            if (isSyntheticClass || isDirectory) {
                println("""
                    Skipping including class: ${change.file.name}. 
                        Details: 
                            Is synthetic: ${isSyntheticClass}, 
                            Is Directory: ${isDirectory}
                """.trimIndent())
                return
            }

            // Only process .class files and exclude .kotlin_module files
            if (change.file.extension == "class") {
                val sourceFile = change.file
                val targetFile = outputDir.file("${sourceFile.name}.hash.txt").get().asFile

                when (change.changeType) {
                    ChangeType.REMOVED -> {
                        if (targetFile.exists()) {
                            targetFile.delete()
                            println("Removed hash report for ${sourceFile.name}")
                        }
                    }
                    ChangeType.ADDED, ChangeType.MODIFIED -> {
                        processFile(sourceFile, targetFile)
                    }
                    else -> {
                        throw RuntimeException("Unknown change type for file detected")
                    }
                }
            }
        }
    }
}


