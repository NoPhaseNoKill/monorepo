package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.dependencies


class BaseKotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        target.pluginManager.apply("com.nophasenokill.group-and-version-plugin")
        target.pluginManager.apply("com.nophasenokill.java-gradle-applier-plugin")
        target.pluginManager.apply("com.nophasenokill.kotlin-dsl-plugin")
        target.pluginManager.apply("com.nophasenokill.publishing-plugin")
        target.pluginManager.apply("com.nophasenokill.repositories-plugin")

        // target.dependencies.add("implementation", "com.nophasenokill:group-and-version-plugin")
        // target.dependencies.add("implementation", "com.nophasenokill:java-gradle-applier-plugin")
        // target.dependencies.add("implementation", "com.nophasenokill:kotlin-dsl-plugin")
        // target.dependencies.add("implementation", "com.nophasenokill:publishing-plugin")

        fun DependencyHandler.`implementation`(dependencyNotation: Any): Dependency? =
            add("implementation", dependencyNotation)

        target.dependencies {
            implementation("org.hamcrest:hamcrest:2.2")
        }

        // target.dependencies.add("implementation", "org.hamcrest:hamcrest:2.2")
        // target.dependencies.add("implementation", "com.nophasenokill:repositories-plugin")



        target.logger.quiet("Applied ${this::class.simpleName}")
        target.logger.quiet("Applied6 ${this::class.simpleName}")
    }
}