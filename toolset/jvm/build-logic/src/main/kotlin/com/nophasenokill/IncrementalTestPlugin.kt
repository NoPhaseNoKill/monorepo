package com.nophasenokill

import com.nophasenokill.extensions.registerAndConfigureTask
import com.nophasenokill.tasks.ComputeClassDigestTask
import com.nophasenokill.tasks.IncrementalTestTask
import com.nophasenokill.tasks.MapTestToClassDigest_v2
import com.nophasenokill.tasks.asm.ComputeInstructionsForSpecificMethodTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register

class IncrementalTestPlugin: Plugin<Project> {
    override fun apply(project: Project) {

            project.run {

                tasks.register<ComputeInstructionsForSpecificMethodTask>("computeInstructionsForSpecificMethodTask") {
                    dependsOn(tasks.withType(JavaCompile::class.java))
                    // Use buildDirectory and resolve to the specific class file
                    val classFile = file("/home/gardo/projects/monorepo/toolset/jvm/build-logic/src/main/kotlin/main/com/nophasenokill/tasks/asm/IdentifyCallExample.class")
                    classFileLocation.set(classFile.absolutePath)

                    // Optional: Print the resolved path for debugging purposes
                    println("Class file location: ${classFile.absolutePath}")
                }

                val computeClassDigests = tasks.register<ComputeClassDigestTask>("computeClassDigests") {

                    /*
                        TODO This does not currently cater for dependencies changing (ie a when using project dependencies)
                            for instance when application-one does:
                                dependencies {
                                    implementation(projects.libraryOne)
                                    testImplementation(projects.testingLibraryOne)
                                }
                            It does not cater for changes in libraryOne or testingLibraryOne even though these alter the tests at runtime
                     */

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
                val test by testing.suites.withType(JvmTestSuite::class.java)

                val incrementalTest = project.registerAndConfigureTask<IncrementalTestTask>("incrementalTest") {
                    dependsOn(mapTestToClassDigests_v2)
                    testClassesDirs = files(test.sources.output.classesDirs )
                    classpath = files(test.sources.runtimeClasspath)
                }
                project.tasks.named("check").get().dependsOn(incrementalTest)
            }
        }

    val Project.testing: org.gradle.testing.base.TestingExtension get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("testing") as org.gradle.testing.base.TestingExtension
}
