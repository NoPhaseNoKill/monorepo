package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.embeddedKotlinVersion

class ProducerPlugin: Plugin<Project> {
    override fun apply(target: Project) {

        /*
            Configures this for the underlying project that uses the plugin.
            These coordinates allow for us to build into '$rootDir/local-repo',
            which in turn allow us to do the following:

                i) Use plugins at the top level of the composite build of a rootProject
                ii) Create plugins within the composite build of a rootProject
                iii) Have them remain on the test classpath,so that we can
                make them infinitely easier to test
         */
        target.apply {
            target.project.group = "com.nophasenokill"
            target.project.version = "1.0.0-local-dev"
        }

        target.run {

            pluginManager.withPlugin("java") {

                dependencies {
                    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${embeddedKotlinVersion}")
                    implementation(gradleApi())
                    testImplementation(gradleTestKit())

                    testImplementation(platform("org.junit:junit-bom:5.10.1"))
                    testImplementation("org.junit.jupiter:junit-jupiter")
                    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

                    pluginManager.apply("com.nophasenokill.standalone-kotlin-base-plugin")
                }
            }

            logger.quiet("I am the producer plugin which is being applied to the ${project.name}")
        }
    }

    private fun DependencyHandler.testRuntimeOnly(dependencyNotation: Any): Dependency? =
        add("testRuntimeOnly", dependencyNotation)


    private fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
        add("testImplementation", dependencyNotation)


    @Suppress("unchecked_cast", "nothing_to_inline")
    private inline fun <T> uncheckedCast(obj: Any?): T = obj as T

    private fun DependencyHandler.project(
        path: String,
        configuration: String? = null
    ): ProjectDependency =

        uncheckedCast(
            project(
                if (configuration != null) mapOf("path" to path, "configuration" to configuration)
                else mapOf("path" to path)
            )
        )

    private fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
        add("implementation", dependencyNotation)

    private fun Project.dependencies(configuration: DependencyHandlerScope.() -> Unit) = DependencyHandlerScope.of(dependencies).configuration()
}