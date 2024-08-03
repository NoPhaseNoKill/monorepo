package com.nophasenokill

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.buildscript
import org.gradle.kotlin.dsl.repositories
import java.net.URI

/*
    This allows us to build gradle plugins, written in kotlin, which have access to the
    compiled kotlin extensions which are commonly used inside of build.gradle.kts (build script) files.

    Commonly, when porting across a build script to a binary plugin, if the kotlin-dsl
    plugin is NOT applied, the source code will not have access to the generated
    kotlin extensions, which make it more cumbersome to develop plugins for.

    There are several huge advantages I have found over time for including/using the kotlin-dsl
    over just the kotlin("jvm") plugin:

        1. Almost all gradle documentation is written from a build script. This means when referencing
        documentation, and copy/pasting code into a binary plugin, it will compile 'as-is' with only
        a small amount of tweaking

        2. If we do not use kotlin-dsl plugin, we need to include or create the extension functions
        ourselves. By doing so, if gradle updates any of these in the future, we may break our
        build in subtle ways or just simply have out-of-date logic/code.

        3. The lambda functions do not work anywhere nearly as well, and it bloats code

    The below code demonstrates the difference in source code (code inside of src/main/kotlin)
    for a developed plugin when the kotlin-dsl is not applied versus when it is applied.

    WITHOUT:

        class SomePlugin: Plugin<Project> {
        override fun apply(target: Project) {

            target.configurations.create("someConfiguration") {
                it.attributes { attribute ->
                    attribute.attribute(Category.CATEGORY_ATTRIBUTE, target.objects.named(Category.DOCUMENTATION))
                    attribute.attribute(DocsType.DOCS_TYPE_ATTRIBUTE, target.objects.named("some-test-report-data"))
                }
            }
        }

        private inline fun <reified T : Named> ObjectFactory.named(name: String): T = named(T::class.java, name)

    WITH:

        class SomePlugin : Plugin<Project> {
            override fun apply(target: Project) {

                target.configurations.create("someConfiguration") {
                    attributes {
                        attribute(Category.CATEGORY_ATTRIBUTE, target.objects.named<Category>(Category.DOCUMENTATION))
                        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, target.objects.named("some-test-report-data"))
                    }
                }
            }
        }
    }
 */

class KotlinDslPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if (!project.pluginManager.hasPlugin("org.gradle.kotlin.kotlin-dsl")) {
            project.pluginManager.apply("org.gradle.kotlin.kotlin-dsl")
        }
    }
}