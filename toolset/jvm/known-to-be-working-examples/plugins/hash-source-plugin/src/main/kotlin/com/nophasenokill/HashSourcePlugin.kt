package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.register

class HashSourcePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register<DirHashTask>("hashSource") {
            /*
                Makes the assumption we have src folder always - but this
                may not be accurate
             */
            contents = project.files(project.layout.projectDirectory.dir("src"))
            hashMethod = "MD5"
            outputDir = project.layout.buildDirectory.dir("src-hash")
        }
    }
}

