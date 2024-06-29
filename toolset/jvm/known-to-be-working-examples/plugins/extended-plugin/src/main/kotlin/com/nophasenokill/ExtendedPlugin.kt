package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class ExtendedPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        /*
            This has transitives of:
                - domain:person (relative to root)
                - domain:account (relative to root)
         */
        project.apply {
            plugin("org.gradle.kotlin.kotlin-dsl")
            plugin("org.gradle.java-gradle-plugin")
            plugin("com.nophasenokill.hash-source-plugin")
        }
    }
}