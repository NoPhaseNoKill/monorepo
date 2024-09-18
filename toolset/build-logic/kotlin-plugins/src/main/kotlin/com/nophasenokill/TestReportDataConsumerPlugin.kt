package com.nophasenokill

import com.nophasenokill.extensions.registerAndConfigureTask
import com.nophasenokill.tasks.ListResolvedArtifacts
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Transformer
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.component.ComponentArtifactIdentifier
import org.gradle.api.artifacts.component.ProjectComponentIdentifier
import org.gradle.api.artifacts.result.ResolvedArtifactResult
import org.gradle.api.artifacts.result.ResolvedVariantResult
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.DocsType
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFile
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.testing.TestReport
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.named
import java.util.stream.Collectors

class TestReportDataConsumerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {

            // Implicitly brings in org.jetbrains.kotlin.dsl
            plugins.apply("org.jetbrains.kotlin.jvm")

            pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
                val layout = project.layout
                val configurations = project.configurations

                class IdExtractor : Transformer<List<ComponentArtifactIdentifier>, Collection<ResolvedArtifactResult>> {

                    override fun transform(artifacts: Collection<ResolvedArtifactResult>): List<ComponentArtifactIdentifier> {
                        return artifacts.stream().map { obj: ResolvedArtifactResult -> obj.id }
                            .collect(Collectors.toList())
                    }
                }

                class VariantExtractor : Transformer<List<ResolvedVariantResult>, Collection<ResolvedArtifactResult>> {
                    override fun transform(artifacts: Collection<ResolvedArtifactResult>): List<ResolvedVariantResult> {
                        return artifacts.stream().map { obj: ResolvedArtifactResult -> obj.variant }
                            .collect(Collectors.toList())
                    }
                }

                class FileExtractor(
                    private val projectLayout: ProjectLayout,
                ) : Transformer<List<RegularFile>, Collection<ResolvedArtifactResult>> {
                    override fun transform(artifacts: Collection<ResolvedArtifactResult>): List<RegularFile> {
                        val projectDirectory = projectLayout.projectDirectory
                        return artifacts.stream()
                            .map { a: ResolvedArtifactResult -> projectDirectory.file(a.file.absolutePath) }.collect(
                                Collectors.toList()
                            )
                    }
                }


                /*
                    Ensures that when we have a project which includes a project dependency, we don't need to add
                    the testReportData configuration.

                    ie:

                        dependencies {
                            implementation(projects.libraryOne)
                            // testDataReport(projects.libraryOne) <----- does this automatically
                        }
                 */
                val listResolvedRuntimeArtifacts = registerAndConfigureTask<ListResolvedArtifacts>("listResolvedRuntimeArtifacts") {

                    val view = configurations.getByName("runtimeClasspath").incoming.artifactView {
                        this.componentFilter {
                            it is ProjectComponentIdentifier
                        }
                    }

                    val artifacts = view.artifacts.resolvedArtifacts

                    artifactIds.set(artifacts.map(IdExtractor()))
                    artifactVariants.set(artifacts.map(VariantExtractor()))
                    artifactFiles.set(artifacts.map(FileExtractor(layout)))
                    outputFile.set(layout.buildDirectory.file("$name/runtime-classpath-report.txt"))
                }

                val listResolvedTestRuntimeArtifacts = registerAndConfigureTask<ListResolvedArtifacts>("listResolvedTestRuntimeArtifacts") {

                    val view = configurations.getByName("testRuntimeClasspath").incoming.artifactView {
                        this.componentFilter {
                            it is ProjectComponentIdentifier
                        }
                    }

                    val artifacts = view.artifacts.resolvedArtifacts

                    artifactIds.set(artifacts.map(IdExtractor()))
                    artifactVariants.set(artifacts.map(VariantExtractor()))
                    artifactFiles.set(artifacts.map(FileExtractor(layout)))
                    outputFile.set(layout.buildDirectory.file("$name/test-runtime-classpath-report.txt"))
                }

                val listResolvedArtifacts = tasks.register("listResolvedArtifacts") {
                    dependsOn(listResolvedRuntimeArtifacts, listResolvedTestRuntimeArtifacts)
                }

                tasks.named("build") {
                    dependsOn(listResolvedArtifacts)
                }
            }

            /*
                There seems to be a weird issue where we need to also wait for the kotlin-dsl, rather than org.jetbrains.kotlin.jvm
                otherwise reporting extension fails
             */
            pluginManager.withPlugin("org.gradle.kotlin.dsl") {

                val testReportData: Configuration by configurations.creating {
                    isCanBeConsumed = false
                    attributes {
                        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
                        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("test-report-data"))
                    }
                }

                /*
                    Adds in the dependencies automatically that we require for our test data so that when we do:
                        dependencies {
                            implementation(projects.libraryOne)
                        }

                        we don't also need to do:

                        dependencies {
                            implementation(projects.libraryOne)
                            testReportData(projects.libraryOne)
                        }

                 */
                // TODO check whether we need both?
                val runtimeClasspath = configurations.getByName("runtimeClasspath")
                val testRuntimeClasspath = configurations.getByName("testRuntimeClasspath")
                val runtimeProjectDependencies = runtimeClasspath.incoming.artifactView { componentFilter { it is ProjectComponentIdentifier } }.files
                val testRuntimeProjectDependencies = testRuntimeClasspath.incoming.artifactView { componentFilter { it is ProjectComponentIdentifier } }.files
                val runtimeClasspathDeps = runtimeProjectDependencies + testRuntimeProjectDependencies

                runtimeClasspathDeps.forEach {
                    project.dependencies.add("testReportData", it)
                }

                val testReportTask = registerAndConfigureTask<TestReport>("testReportTask") {
                    val reportExtension = requireNotNull(extensions.findByType(ReportingExtension::class.java)) {
                        "Expected to find reporting extension and could not be found"
                    }

                    destinationDirectory.set(reportExtension.baseDirectory.dir("allTests"))
                    // Use test results from testReportData configuration
                    testResults.from(testReportData)
                }

                tasks.named("build") {
                    dependsOn(testReportTask)
                }

            }
        }
    }
}
