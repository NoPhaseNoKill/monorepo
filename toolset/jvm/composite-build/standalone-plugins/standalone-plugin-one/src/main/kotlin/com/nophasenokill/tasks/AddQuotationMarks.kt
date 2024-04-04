package com.nophasenokill.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files


internal abstract class AddQuotationMarks : DefaultTask() {
    @get:InputFile
    abstract val inputFile: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    @Throws(IOException::class)
    fun join() {


        val inputFileContents: String = Files.readString(inputFile.get().asFile.toPath())

        FileWriter(outputFile.get().asFile).use { fileWriter ->
            fileWriter.write("\"")
            fileWriter.write(inputFileContents)
            fileWriter.write("\"")
        }
    }
}