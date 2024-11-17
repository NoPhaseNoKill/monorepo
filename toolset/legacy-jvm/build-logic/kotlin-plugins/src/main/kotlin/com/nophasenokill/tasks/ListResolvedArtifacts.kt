package com.nophasenokill.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.component.ComponentArtifactIdentifier
import org.gradle.api.artifacts.result.ResolvedVariantResult
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Files

abstract class ListResolvedArtifacts : DefaultTask() {
    @get:Input
    abstract val artifactIds: ListProperty<ComponentArtifactIdentifier>

    @get:Input
    abstract val artifactVariants: ListProperty<ResolvedVariantResult>

    @get:InputFiles
    abstract val artifactFiles: ListProperty<RegularFile>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    @Throws(IOException::class)
    fun action() {
        val outputFile = outputFile.asFile.get()
        PrintWriter(FileWriter(outputFile)).use { writer ->
            val ids = artifactIds.get()
            val variants = artifactVariants.get()
            val files = artifactFiles.get()
            for (index in ids.indices) {
                val id = ids[index]
                val variant = variants[index]
                val file = files[index]
                writer.print("FILE ")
                writer.println(file.asFile.name)
                writer.print("  id: ")
                writer.println(id.displayName)
                writer.print("  variant: ")
                writer.println(variant.displayName)
                writer.print("  size: ")
                writer.println(file.asFile.length())
                writer.println()
            }
        }
        Files.lines(outputFile.toPath()).use { stream ->
            stream.forEach { x: String? -> println(x) }
        }
    }
}
