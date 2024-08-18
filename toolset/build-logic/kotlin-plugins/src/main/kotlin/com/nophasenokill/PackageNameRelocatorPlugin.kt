package com.nophasenokill

import com.nophasenokill.extensions.sourceSets
import com.nophasenokill.tasks.ClassRelocatorTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Attribute
import org.gradle.api.tasks.bundling.Jar
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registerTransform
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import java.io.File
import java.util.jar.JarFile

/*
    Showcases how we can relocate the package name of an application.
    The intention In future is that we will migrate this to the standard kotlin
    plugin, which will enable us to not have to add com/nophasenokill
    package name nesting - as it will automatically transform them
    correctly for us - removing the need for unnecessary/duplicate folder
    hierarchies.

    Currently, when you run build followed by testRelocation tasks, you
    should get something like:

    > Task :standalone-projects:applications:some-relocation-test:testRelocation
        Sample JAR file created at: ${rootDir}\standalone-projects\applications\some-relocation-test\build\libs\example-package-name-relocation-app-0.1.local-dev.jar
        Primary input file: ${rootDir}\standalone-projects\applications\some-relocation-test\build\libs\example-package-name-relocation-app-0.1.local-dev.jar
        Output file: ${gradleHomeDir}\.gradle\caches\8.9\transforms\9336bff1a1b79fe0060edd4cc60f713c-c795024b-04e4-4d50-b2b1-a7d5aff996c0\transformed\sample-0.1.local-dev-relocated.jar
        com.nophasenokill -> relocated.com.nophasenokill
        Relocations: [me.lucko.jarrelocator.Relocation@d9c2800]
        Starting relocation process...
        Relocation process completed.
        Transformed files: [${gradleHomeDir}\.gradle\caches\8.9\transforms\9336bff1a1b79fe0060edd4cc60f713c\transformed\sample-0.1.local-dev-relocated.jar]
        Relocation successful: [relocated.com.nophasenokill]
        Class in relocated JAR: relocated/com/nophasenokill/SampleClass.class
 */
class PackageNameRelocatorPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        project.run {
            group = "com.nophasenokill"
            version = "0.1.local-dev"

            plugins.apply("org.jetbrains.kotlin.jvm")

            val kotlinJvmProjectExtension = extensions.findByType(KotlinJvmProjectExtension::class.java)

            kotlinJvmProjectExtension?.run {

                jvmToolchain {
                    languageVersion.set(JavaLanguageVersion.of(17))
                }
            }


            configurations.create("externalClasspath")

            val usage = Attribute.of("usage", String::class.java)
            val artifactType = Attribute.of("artifactType", String::class.java)

            dependencies {
                add("runtimeOnly", "me.lucko:jar-relocator:1.7")
                add("implementation", "org.ow2.asm:asm:9.7")
                registerTransform(ClassRelocatorTask::class) {
                    from.attribute(artifactType, "jar")
                    to.attribute(artifactType, "relocated-classes")
                    parameters {
                        externalClasspath.from(configurations.getByName("externalClasspath"))
                        excludedPackage.set("org.gradle.api")
                    }
                }

                attributesSchema {
                    attribute(usage)
                }
            }

            configurations.create("compile") {
                attributes.attribute(usage, "api")
            }

            tasks.register<Jar>("createSampleJar") {
                archiveBaseName.set(project.name)
                from(project.sourceSets["main"].output)
            }


            fun File.readPackages(): Set<String> {
                JarFile(this).use { jarFile ->
                    return jarFile.entries().asSequence()
                        .filter { !it.isDirectory }
                        .filter { it.name.endsWith(".class") }
                        .map { entry ->
                            entry.name.substringBeforeLast('/').replace('/', '.')
                        }
                        .toSet()
                }
            }

            tasks.register("testRelocation") {
                dependsOn("createSampleJar")

                doLast {
                    val jarFile = file("${layout.buildDirectory.asFile.get().path}/libs/${project.name}-0.1.local-dev.jar")
                    println("Sample JAR file created at: $jarFile")

                    val tempConfig = configurations.detachedConfiguration(
                        dependencies.create(files(jarFile))
                    ).apply {
                        attributes {
                            attribute(artifactType, "jar")
                        }
                    }

                    val transformedFiles = tempConfig.incoming.artifactView {
                        attributes {
                            attribute(artifactType, "relocated-classes")
                        }
                    }.artifacts.artifactFiles

                    println("Transformed files: ${transformedFiles.files}")
                    assert(transformedFiles.files.size == 1) { "Expected one relocated file, but found ${transformedFiles.files.size}" }
                    val relocatedFile = transformedFiles.singleFile

                    // Verify the relocation
                    val relocatedPackages = relocatedFile.readPackages()
                    assert(relocatedPackages.contains("relocated.com.nophasenokill")) {
                        "Relocation failed: $relocatedPackages"
                    }
                    println("Relocation successful: $relocatedPackages")

                    // Display relocated classes
                    JarFile(relocatedFile).use { jar ->
                        jar.entries().asSequence()
                            .filter { !it.isDirectory && it.name.endsWith(".class") }
                            .forEach { entry ->
                                println("Class in relocated JAR: ${entry.name}")
                            }
                    }
                }

            }
        }

    }


}


