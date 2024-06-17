package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.component.AdhocComponentWithVariants
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.repositories


class PublishingPlugin : Plugin<Project> {
    override fun apply(target: Project) {


        target.pluginManager.apply("org.gradle.maven-publish")
        target.logger.quiet("Applied ${this::class.simpleName}")

        val javaExtension = target.extensions.findByType(JavaPluginExtension::class.java)

        javaExtension?.run {
                withJavadocJar()
                /*
                    Configures an output jar containing the source code
                    as opposed to just the compiled code. This helps
                    developers read/find source code more easily.

                    This method under the hood adds a new configuration,
                    and hence also adds to the attribute of docstype.

                    This docstype is used for variant selection and an
                    example can be seen here:

                        Variant sourcesElements
                        --------------------------------------------------
                        sources elements for main.

                        Capabilities
                            - com.nophasenokill:publishing-plugin-one:1.0.0-local-dev (default capability)
                        Attributes
                            - org.gradle.category            = documentation
                            - org.gradle.dependency.bundling = external
                            - org.gradle.docstype            = sources
                            - org.gradle.usage               = java-runtime
                        Artifacts
                            - build/libs/publishing-plugin-one-1.0.0-local-dev-sources.jar (artifactType = jar, classifier = sources)

                 */
                withSourcesJar()

            val mavenPublishingExtension = target.extensions.findByType(PublishingExtension::class.java)

            mavenPublishingExtension?.run {
                publications {
                    create("mavenJava", MavenPublication::class.java) {
                        from(target.components["java"])
                    }
                }

                repositories {
                    maven {
                        url = target.uri("${target.rootProject.projectDir}/local-repo")

                        /*
                                        Because we control all the modules we publish, we can assert
                                        that they always contain gradle metadata. By adding the below,
                                        we now get optimized network calls.

                                        See: https://docs.gradle.org/8.8/userguide/publishing_gradle_module_metadata.html#sub:interactions-other-build-tools
                                     */
                        metadataSources {
                            gradleMetadata()
                        }
                    }
                }
            }
        }
    }
}