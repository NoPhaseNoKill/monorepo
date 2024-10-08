package com.nophasenokill

import com.nophasenokill.extensions.registerAndConfigureTask
import com.nophasenokill.transforms.Minify
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Attribute
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.dependencies
import org.gradle.api.artifacts.ProjectDependency

class MinifyExternalDependenciesPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            val artifactType = Attribute.of("artifactType", String::class.java)
            val minified = Attribute.of("minified", Boolean::class.javaObjectType)
            dependencies {
                attributesSchema {
                    attribute(minified)
                }
                artifactTypes.getByName("jar") {
                    attributes.attribute(minified, false)
                }
            }

            configurations.all {
                afterEvaluate {
                    if (isCanBeResolved) {
                        incoming.dependencies.forEach { dependency ->
                            if (dependency is ProjectDependency) {
                                // Skip minification for project dependencies so we can still debug
                                attributes.attribute(minified, false)
                            } else {
                                // Apply minification for external dependencies
                                attributes.attribute(minified, true)
                            }
                        }
                    }
                }
            }

            dependencies {
                registerTransform(Minify::class.java) {
                    from.attribute(minified, false).attribute(artifactType, "jar")
                    to.attribute(minified, true).attribute(artifactType, "jar")
                }
            }

            dependencies {
                add("implementation", "com.google.guava:guava:27.1-jre")
                // add("implementation", project(":meta-gradle-utilities"))
                add("implementation", "com.nophasenokill.meta-gradle-utilities:meta-gradle-utilities:0.1.local-dev")
            }

            registerAndConfigureTask<Copy>("resolveRuntimeClasspath") {
                from(configurations.named("runtimeClasspath"))
                into(layout.buildDirectory.dir("runtimeClasspath"))
            }
        }
    }
}
