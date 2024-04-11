package com.nophasenokill

import com.nophasenokill.tasks.AddQuotationMarks
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider


internal class WrapTextWithQuotesPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("addQuotationMarks", AddQuotationMarks::class.java) {
            it.inputFile.set(project.layout.projectDirectory.file("example-text.txt"))
            it.outputFile.set(project.layout.projectDirectory.file("example-text-output-pre-quote-adding.txt"))
        }
    }
}