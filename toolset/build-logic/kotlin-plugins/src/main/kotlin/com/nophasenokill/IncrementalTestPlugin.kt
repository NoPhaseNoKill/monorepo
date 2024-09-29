package com.nophasenokill

import MapTestToClassDigest_v1
import com.nophasenokill.extensions.registerAndConfigureTask
import com.nophasenokill.tasks.*
import com.nophasenokill.tasks.asm.ComputeInstructionsForSpecificMethodTask
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.reflect.KClass

class IncrementalTestPlugin: Plugin<Project> {
    override fun apply(project: Project) {

            project.run {

                tasks.register<ComputeInstructionsForSpecificMethodTask>("computeInstructionsForSpecificMethodTask") {
                    dependsOn(tasks.withType(JavaCompile::class.java))
                    // Use buildDirectory and resolve to the specific class file
                    val classFile = file("/home/gardo/projects/monorepo/toolset/build-logic/kotlin-plugins/build/classes/kotlin/main/com/nophasenokill/tasks/asm/IdentifyCallExample.class")
                    classFileLocation.set(classFile.absolutePath)

                    // Optional: Print the resolved path for debugging purposes
                    println("Class file location: ${classFile.absolutePath}")
                }

                val computeClassDigests = tasks.register<ComputeClassDigestTask>("computeClassDigests") {
                    classFiles.setFrom(fileTree(layout.buildDirectory.dir("classes")) {
                        include("**/*.class")
                    })

                    outputFile.set(layout.buildDirectory.file("computeClassDigests.txt"))

                    dependsOn(tasks.withType(JavaCompile::class.java)) // Ensure it runs after compilation
                }

                val mapTestToClassDigests_v2 = tasks.register<MapTestToClassDigest_v2>("mapTestToClassDigests_v2") {
                    digestFile.set(computeClassDigests.flatMap { it.outputFile })

                    testClasses.setFrom(fileTree(layout.buildDirectory.dir("classes")) {
                        include("**/*Test.class")
                    })

                    testToDigestMapFile.set(layout.buildDirectory.file("testToDigestMap.txt"))

                    val previousDigestFileLocation = "${layout.buildDirectory.asFile.get().absolutePath}/previousTestDigest.txt"
                    previousTestDigestFileLocation.set(previousDigestFileLocation)

                    testsNeedRerunFile.set(layout.buildDirectory.file("testsNeedRerun.txt"))
                    testsNeedRerunReasonFile.set(layout.buildDirectory.file("testsNeedRerunReason.txt"))

                    dependsOn(computeClassDigests)
                }

                /*
                    Fixes: https://docs.gradle.org/8.10.1/userguide/upgrading_version_8.html#test_task_default_classpath
                 */
                val test by testing.suites.existing(JvmTestSuite::class)

                val incrementalTest = project.registerAndConfigureTask<IncrementalTestTask>("incrementalTest") {
                    dependsOn(mapTestToClassDigests_v2)
                    testClassesDirs = files(test.map { it.sources.output.classesDirs })
                    classpath = files(test.map { it.sources.runtimeClasspath })
                }
                project.tasks.named("check").get().dependsOn(incrementalTest)
            }
        }

    val Project.`testing`: org.gradle.testing.base.TestingExtension get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("testing") as org.gradle.testing.base.TestingExtension

    fun <T : Any, C : NamedDomainObjectCollection<T>, U : T> C.existing(type: KClass<U>): ExistingDomainObjectDelegateProviderWithType<out C, U> =
        ExistingDomainObjectDelegateProviderWithType.of(this, type)
}
