package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.kotlin.dsl.create

class LocalPublishingPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            plugins.apply(PublishingPlugin::class.java)
            plugins.apply(MavenPublishPlugin::class.java)

            afterEvaluate {
                val publishingExtension = extensions.findByType(PublishingExtension::class.java)
                publishingExtension?.publications {
                    create<MavenPublication>("mavenJava") {
                        /*
                             Configures the resolution in each of the dependencies to be the same so we don't get misconfigurations
                             when publishing
                          */
                        versionMapping {
                            usage("java-api") {
                                fromResolutionOf("runtimeClasspath")
                            }
                            usage("java-runtime") {
                                fromResolutionResult()
                            }
                        }
                    }

                    repositories.maven {
                        url = uri(isolated.rootProject.projectDirectory.dir("build/local-repos"))
                    }
                }
            }
        }
    }
}
