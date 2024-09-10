package com.nophasenokill

import com.nophasenokill.extensions.configureTask
import com.nophasenokill.extensions.registerAndConfigureTask
import com.nophasenokill.extensions.sourceSets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import java.util.*

class KotlinApplicationPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            plugins.apply("com.nophasenokill.kotlin-base-plugin")
            plugins.apply("application")
            plugins.apply("com.nophasenokill.jacoco-plugin")
            plugins.apply("com.nophasenokill.idea-sources-download-plugin")


            val mainClassName = projectDir.name.split("-").joinToString("") { it ->
                it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            } + "AppKt"

            val kotlinJvmProjectExtension = extensions.findByType(JavaApplication::class.java)

            kotlinJvmProjectExtension?.run {
                mainClass.set("com.nophasenokill.${mainClassName}")
            }

            // pluginManager.withPlugin("java") {
            //
            //     /*
            //         Please note this is getAllJava(), NOT to be confused with getJava() (very easy to misunderstand).
            //
            //         Using getAllJava() ensures that we capture all Java source files that might be involved in
            //         the project's compilation, including those that are part of joint compilation with Kotlin.
            //
            //         This is important for comprehensive instrumentation, as it ensures no relevant Java source files
            //         are missed.
            //      */
            //     val allJavaSourceSet = project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.getByName("main").allJava
            //     val javaTestSourceSet = project.extensions.getByType(JavaPluginExtension::class.java).sourceSets.getByName("test").java
            //
            //
            //
            //     val applicationInstrumentationTask = registerAndConfigureTask<InstrumentApplicationTask>("applicationInstrumentationTask") {
            //         dependsOn(allJavaSourceSet, "compileKotlin", "compileTestKotlin", "processTestResources", "compileTestJava")
            //         inputDir.set(project.layout.buildDirectory)
            //         appName.set(mainClassName)
            //         appClassResourceName.set("com/nophasenokill/${mainClassName}.class")
            //         outputDir.set(project.layout.buildDirectory.dir("instrumented-task-output"))
            //     }
            //
            //     // configureTask<Test>("test") {
            //     //     dependsOn(applicationInstrumentationTask)
            //     //
            //     //     val instrumentedDir = project.layout.buildDirectory.dir("instrumented-task-output")
            //     //
            //     //     doFirst {
            //     //         classpath = files(instrumentedDir) + classpath
            //     //     }
            //     // }
            //     //
            //     //
            //     // // Configures the test source set to use the instrumented version of our app
            //     // javaTestSourceSet.srcDirs(project.layout.buildDirectory.dir("instrumented-task-output"))
            // }
        }
    }
}
