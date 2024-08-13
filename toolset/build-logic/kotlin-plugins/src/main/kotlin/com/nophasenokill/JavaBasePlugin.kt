package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class JavaBasePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            group = "com.nophasenokill"
            version = "0.1.local-dev"
        }
    }
}