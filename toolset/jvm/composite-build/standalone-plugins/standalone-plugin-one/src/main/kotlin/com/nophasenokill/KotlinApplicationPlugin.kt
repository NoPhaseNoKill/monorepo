package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.internal.artifacts.DefaultModuleIdentifier
import org.gradle.api.internal.artifacts.dependencies.DefaultMinimalDependency
import org.gradle.api.internal.artifacts.dependencies.DefaultMutableVersionConstraint
import org.gradle.api.plugins.JavaApplication

class KotlinApplicationPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
        project.pluginManager.apply("application")
        project.pluginManager.apply("com.nophasenokill.meta-plugins.check-kotlin-build-service-fix-plugin")



        project.extensions.getByType(JavaApplication::class.java).mainClass.set("com.nophasenokill.App")

        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {

            project.addMetaPluginDependency("meta-plugin-one")
            project.addPlatformDependency("implementation", "com.nophasenokill.platforms", "generalised-platform")

            project.dependencies.add("implementation", "org.slf4j:slf4j-api").apply {
                if (this is ExternalModuleDependency) {
                    val slf4jExclusion = mapOf(
                        "group" to "org.slf4j",
                        "module" to "slf4j-simple"
                    )

                    this.exclude(
                        slf4jExclusion
                    )
                }
            }

            project.dependencies.add("runtimeOnly", "org.slf4j:slf4j-simple")
            project.dependencies.add("testRuntimeOnly", "org.slf4j:slf4j-simple")


            project.repositories.gradlePluginPortal()
        }
    }

    private fun Project.addMetaPluginDependency(plugin: String) {
        val moduleId = DefaultModuleIdentifier.newId("com.nophasenokill.meta-plugins", plugin)
        val versionConstraint = DefaultMutableVersionConstraint("")
        val dependency: Dependency = DefaultMinimalDependency(moduleId, versionConstraint)

        project.configurations.findByName("implementation")?.dependencies?.add(dependency)
    }


    private fun Project.addPlatformDependency(configuration: String, group: String, name: String) {
        val moduleId = DefaultModuleIdentifier.newId(group, name)
        val versionConstraint = DefaultMutableVersionConstraint("")
        val dependency: Dependency = DefaultMinimalDependency(moduleId, versionConstraint)

        project.configurations.findByName(configuration)?.dependencies?.add(project.dependencies.platform(dependency))
    }
}