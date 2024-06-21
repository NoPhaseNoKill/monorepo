package com.nophasenokill

import org.gradle.api.DefaultTask
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.api.attributes.java.TargetJvmVersion
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin.PUBLISH_LOCAL_LIFECYCLE_TASK_NAME
import org.gradle.api.publish.maven.tasks.PublishToMavenLocal
import org.gradle.api.publish.plugins.PublishingPlugin.PUBLISH_LIFECYCLE_TASK_NAME
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.named

class ProducerConfigurationPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        target.logger.quiet("Applied ${this::class.simpleName}")
        target.plugins.apply("org.gradle.java-gradle-plugin")
        target.plugins.apply("org.gradle.maven-publish")

        target.tasks.withType(DefaultTask::class.java).named("build").configure {
            val build = this
            target.tasks.withType(PublishToMavenLocal::class.java).configureEach {
                build.dependsOn(this)
            }
        }

        val mavenPublishingExtension = target.extensions.getByType(PublishingExtension::class.java)

        mavenPublishingExtension.repositories {
            maven {
                url = target.uri(target.rootDir.resolve("maven-repo"))
            }
        }

        val instrumentedJars by target.configurations.creating {
            isCanBeConsumed = true
            isCanBeResolved = false
            attributes {
                attribute(Category.CATEGORY_ATTRIBUTE, target.objects.named(Category.LIBRARY))
                attribute(Usage.USAGE_ATTRIBUTE, target.objects.named(Usage.JAVA_RUNTIME))
                attribute(Bundling.BUNDLING_ATTRIBUTE, target.objects.named(Bundling.EXTERNAL))
                attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, JavaVersion.current().majorVersion.toInt())
                attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, target.objects.named("instrumented-jar"))
            }
        }

        target.artifacts {
            add(instrumentedJars.name, target.tasks.named("jar")) {
                /*
                    Only need to declare this if the task we're using DOES NOT generate a jar,
                    do this anyway for re-use/to be careful

                    TODO do we actually want PUBLISH_LIFECYCLE_TASK_NAME here instead?
                 */
                builtBy(target.tasks.named(PUBLISH_LOCAL_LIFECYCLE_TASK_NAME))
            }
        }
    }

}