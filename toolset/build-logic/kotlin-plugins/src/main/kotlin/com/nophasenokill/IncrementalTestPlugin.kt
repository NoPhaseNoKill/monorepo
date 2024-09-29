package com.nophasenokill

import com.nophasenokill.extensions.registerAndConfigureTask
import com.nophasenokill.tasks.IncrementallyHashBytecodeFilesToMD5Task
import com.nophasenokill.tasks.IncrementalTestTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class IncrementalTestPlugin: Plugin<Project> {
    override fun apply(project: Project) {

            project.run {
                val hashBytecodeFiles = tasks.register<IncrementallyHashBytecodeFilesToMD5Task>("hashBytecodeFiles") {

                    // Ensure this task depends on any compilation tasks, to make sure the class files are generated
                    dependsOn(tasks.withType(KotlinCompile::class.java), tasks.withType(JavaCompile::class.java))

                    val classesDir = layout.buildDirectory.dir("classes")
                    inputDir.set(classesDir)
                    outputDir.set(layout.buildDirectory.dir("fileHashOutput"))
                }


                val incrementalTest = project.registerAndConfigureTask<IncrementalTestTask>("incrementalTest") {

                }

                project.tasks.named("check").get().dependsOn(incrementalTest)
                project.tasks.named("build").get().dependsOn(hashBytecodeFiles)
            }
        }
}
