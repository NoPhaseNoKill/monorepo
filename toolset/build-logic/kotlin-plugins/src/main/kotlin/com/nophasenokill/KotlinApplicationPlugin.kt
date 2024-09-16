package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import java.util.*

class KotlinApplicationPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            plugins.apply("com.nophasenokill.kotlin-base-plugin")
            plugins.apply("application")
            plugins.apply("com.nophasenokill.idea-sources-download-plugin")



            val mainClassName = projectDir.name.split("-").joinToString("") { it ->
                it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            } + "AppKt"

            val kotlinJvmProjectExtension = extensions.findByType(JavaApplication::class.java)

            kotlinJvmProjectExtension?.run {
                mainClass.set("com.nophasenokill.${mainClassName}")
            }
        }
    }
}
