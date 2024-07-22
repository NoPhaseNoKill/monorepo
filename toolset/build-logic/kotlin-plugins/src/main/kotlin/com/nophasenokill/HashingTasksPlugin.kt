package com.nophasenokill

import com.nophasenokill.extensions.registerAndConfigureTask
import com.nophasenokill.tasks.DirHashTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class HashingTasksPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            registerAndConfigureTask<DirHashTask>("hashKotlinSourceSet") {
                val kotlinSourceSet = project.extensions.getByType(KotlinJvmProjectExtension::class.java).sourceSets.getByName("main")
                contents.set(kotlinSourceSet.kotlin.classesDirectory)
                hashMethod.set("MD5")
                outputDir.set(project.layout.buildDirectory.dir("outputs/source-hash"))
            }
        }
    }
}