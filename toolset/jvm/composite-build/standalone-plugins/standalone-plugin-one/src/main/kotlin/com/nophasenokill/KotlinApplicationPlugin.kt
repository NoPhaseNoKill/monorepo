package com.nophasenokill

import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication

class KotlinApplicationPlugin: KotlinBase() {
    override fun apply(project: Project) {

        super.apply(project)

        project.pluginManager.apply("application")

        project.pluginManager.withPlugin("application") {
            project.extensions.getByType(JavaApplication::class.java).mainClass.set("com.nophasenokill.App")
        }
    }
}