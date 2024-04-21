package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.internal.artifacts.DefaultModuleIdentifier
import org.gradle.api.internal.artifacts.dependencies.DefaultMinimalDependency
import org.gradle.api.internal.artifacts.dependencies.DefaultMutableVersionConstraint

class MetaPluginOne: Plugin<Project> {
    override fun apply(target: Project) {
        println("Applying meta-plugin-one")

        target.repositories.mavenCentral()

        target.pluginManager.withPlugin("java") {
            target.dependencies.add("implementation", target.dependencies.platform("com.nophasenokill.platforms:generalised-platform"))
            target.dependencies.add("implementation", target.dependencies.platform("org.jetbrains.kotlin:kotlin-bom"))
        }

        if(target.configurations.findByName("kotlinBuildToolsApiClasspath") != null) {
            val buildToolConfig = target.configurations.findByName("kotlinBuildToolsApiClasspath")
            buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinBuildToolsApiClasspath", target.dependencies.platform("com.nophasenokill.platforms:generalised-platform")))
            buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinBuildToolsApiClasspath", target.dependencies.platform("org.jetbrains.kotlin:kotlin-bom")))
            buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinBuildToolsApiClasspath", "org.jetbrains.kotlin:kotlin-reflect:1.9.23"))

        }

        if(target.configurations.findByName("kotlinCompilerClasspath") != null) {
            val buildToolConfig = target.configurations.findByName("kotlinCompilerClasspath")
            buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinCompilerClasspath", target.dependencies.platform("com.nophasenokill.platforms:generalised-platform")))
            buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinCompilerClasspath", target.dependencies.platform("org.jetbrains.kotlin:kotlin-bom")))
            buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinCompilerClasspath", "org.jetbrains.kotlin:kotlin-reflect:1.9.23"))
        }

        if(target.configurations.findByName("kotlinKlibCommonizerClasspath") != null) {
            val buildToolConfig = target.configurations.findByName("kotlinKlibCommonizerClasspath")
            buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinKlibCommonizerClasspath", target.dependencies.platform("com.nophasenokill.platforms:generalised-platform")))
            buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinKlibCommonizerClasspath", target.dependencies.platform("org.jetbrains.kotlin:kotlin-bom")))
            buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinKlibCommonizerClasspath", "org.jetbrains.kotlin:kotlin-reflect:1.9.23"))
        }

        target.tasks.register("meta-plugin-one-specific-task") {
            it.doLast {
                it.logger.lifecycle("Running task: meta-plugin-one-specific-task, from meta-plugin-one")
            }
        }
    }
}