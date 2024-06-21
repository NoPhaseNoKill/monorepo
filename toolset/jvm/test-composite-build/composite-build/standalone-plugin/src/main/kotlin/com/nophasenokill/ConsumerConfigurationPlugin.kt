package com.nophasenokill

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.AttributeCompatibilityRule
import org.gradle.api.attributes.CompatibilityCheckDetails
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.publish.maven.tasks.PublishToMavenLocal

class ConsumerConfigurationPlugin: Plugin<Project> {
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

        /*
            Enables usage the regular jar, if no instrumented version is available
         */
        abstract class InstrumentedJarsRule: AttributeCompatibilityRule<LibraryElements> {

            override fun execute(details: CompatibilityCheckDetails<LibraryElements>) = details.run {
                if (consumerValue?.name == "instrumented-jar" && producerValue?.name == "jar") {
                    compatible()
                }
            }
        }

        /*
            Configures the variant so that runtimeElements isn't
            selected, and that our producer variant is instead of it.

            By doing this we:

            1. Consume the variant of the instrumented jars
            2. Explain that this variant is a substitute for the runtime
            3. Explain that the consumer needs this variant only for test runtime
            4. If no variant exists, will select the default jar

         */
        target.dependencies.add("implementation","com.nophasenokill:standalone-plugin")
        target.dependencies
            .attributesSchema
            .attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE)
            .compatibilityRules
            .add(InstrumentedJarsRule::class.java)

        /*
            Enables usage the regular jar, if no instrumented version is available
         */
        target
            .configurations
            .findByName("testRuntimeClasspath")
            ?.attributes
            ?.attribute(
                LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE,
                target.objects.named(LibraryElements::class.java, "instrumented-jar")
            )

        //for the producer plugin
        target.dependencies.add("testImplementation", "com.nophasenokill:standalone-plugin")
    }
}