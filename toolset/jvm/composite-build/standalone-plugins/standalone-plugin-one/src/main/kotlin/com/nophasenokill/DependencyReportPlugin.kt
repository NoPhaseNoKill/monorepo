package com.nophasenokill

import com.nophasenokill.DependencyReportPlugin.*
import org.gradle.api.*
import org.gradle.api.artifacts.component.ComponentArtifactIdentifier
import org.gradle.api.artifacts.component.ComponentIdentifier
import org.gradle.api.artifacts.result.ResolvedArtifactResult
import org.gradle.api.artifacts.result.ResolvedComponentResult
import org.gradle.api.artifacts.result.ResolvedDependencyResult
import org.gradle.api.artifacts.result.ResolvedVariantResult
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Files
import java.util.stream.Collectors


abstract class DependencyReportPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.withPlugin("java-base") { _: AppliedPlugin? ->
            val layout = project.layout
            val configurations = project.configurations
            val tasks = project.tasks

            tasks.register(
                "listResolvedArtifacts",
                ListResolvedArtifacts::class.java
            ) { task: ListResolvedArtifacts ->
                val runtimeClasspath =
                    configurations.getByName("runtimeClasspath")
                val artifacts =
                    runtimeClasspath.incoming.artifacts.resolvedArtifacts

                task.artifactIds.set(
                    artifacts.map(
                        IdExtractor()
                    )
                )


                task.artifactVariants.set(
                    artifacts.map(
                        VariantExtractor()
                    )
                )
                task.artifactFiles.set(
                    artifacts.map(
                        FileExtractor(layout)
                    )
                )
                task.outputFile.set(layout.buildDirectory.file(task.name + "/report.txt"))
            }

            tasks.register(
                "graphResolvedComponents",
                GraphResolvedComponents::class.java
            ) { task: GraphResolvedComponents ->
                val runtimeClasspath =
                    configurations.getByName("runtimeClasspath")
                task.rootComponent.set(
                    runtimeClasspath.incoming.resolutionResult.rootComponent
                )
                task.outputFile.set(layout.buildDirectory.file(task.name + "/report.txt"))
            }
            tasks.register("graphResolvedComponentsAndFiles",
                GraphResolvedComponentsAndFiles::class.java,
                Action<GraphResolvedComponentsAndFiles> { task: GraphResolvedComponentsAndFiles ->
                    val resolvableDependencies =
                        configurations.getByName("runtimeClasspath").incoming
                    val resolvedArtifacts =
                        resolvableDependencies.artifacts.resolvedArtifacts

                    task.artifactFiles.from(resolvableDependencies.artifacts.artifactFiles)
                    task.artifactIdentifiers.set(
                        resolvedArtifacts.map { result: Set<ResolvedArtifactResult> ->
                            result.stream()
                                .map { obj: ResolvedArtifactResult -> obj.id }
                                .collect(
                                    Collectors.toList()
                                )
                        }
                    )
                    task.rootComponent.set(resolvableDependencies.resolutionResult.rootComponent)
                    task.outputFile.set(layout.buildDirectory.file(task.name + "/report.txt"))
                })
        }
    }

    internal class IdExtractor

        :
        Transformer<List<ComponentArtifactIdentifier>, Collection<ResolvedArtifactResult>> {
        override fun transform(artifacts: Collection<ResolvedArtifactResult>): List<ComponentArtifactIdentifier> {
            return artifacts.stream().map { obj: ResolvedArtifactResult -> obj.id }
                .collect(Collectors.toList())
        }
    }

    internal class VariantExtractor :
        Transformer<List<ResolvedVariantResult>, Collection<ResolvedArtifactResult>> {
        override fun transform(artifacts: Collection<ResolvedArtifactResult>): List<ResolvedVariantResult> {
            return artifacts.stream().map { obj: ResolvedArtifactResult -> obj.variant }
                .collect(Collectors.toList())
        }
    }

    internal class FileExtractor(private val projectLayout: ProjectLayout) :
        Transformer<List<RegularFile>, Collection<ResolvedArtifactResult>> {
        override fun transform(artifacts: Collection<ResolvedArtifactResult>): List<RegularFile> {
            val projectDirectory = projectLayout.projectDirectory
            return artifacts.stream().map { a: ResolvedArtifactResult ->
                projectDirectory.file(
                    a.file.absolutePath
                )
            }.collect(Collectors.toList())
        }
    }
}

internal abstract class GraphResolvedComponents : DefaultTask() {
    @get:Input
    abstract val rootComponent: Property<ResolvedComponentResult?>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    @Throws(IOException::class)
    fun action() {
        val outputFile = outputFile.get().asFile
        PrintWriter(outputFile).use { writer ->
            val seen: MutableSet<ResolvedComponentResult> =
                HashSet()
            reportComponent(requireNotNull(rootComponent.get()), writer, seen, "")
        }
        Files.lines(outputFile.toPath()).use { stream ->
            stream.forEach { x: String? ->
                println(
                    x
                )
            }
        }
    }

    private fun reportComponent(
        component: ResolvedComponentResult,
        writer: PrintWriter,
        seen: MutableSet<ResolvedComponentResult>,
        indent: String
    ) {
        writer.print(component.id.displayName)
        if (seen.add(component)) {
            writer.println()
            val newIndent = "$indent  "
            for (dependency in component.dependencies) {
                writer.print(newIndent)
                writer.print(dependency.requested.displayName)
                writer.print(" -> ")
                if (dependency is ResolvedDependencyResult) {
                    reportComponent(dependency.selected, writer, seen, newIndent)
                } else {
                    writer.println(" -> not found")
                }
            }
        } else {
            writer.println(" (already seen)")
        }
    }
}


internal abstract class GraphResolvedComponentsAndFiles : DefaultTask() {
    @get:Input
    abstract val artifactIdentifiers: ListProperty<ComponentArtifactIdentifier>

    @get:InputFiles
    abstract val artifactFiles: ConfigurableFileCollection

    @get:Input
    abstract val rootComponent: Property<ResolvedComponentResult?>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    @Throws(IOException::class)
    fun action() {
        val filesByIdentifiers = filesByIdentifiers()
        val outputFile = outputFile.get().asFile
        PrintWriter(outputFile).use { writer ->
            val seen: MutableSet<ResolvedComponentResult> =
                HashSet()
            reportComponent(requireNotNull(rootComponent.get()), writer, seen, filesByIdentifiers, "")
        }
        Files.lines(outputFile.toPath()).use { stream ->
            stream.forEach { x: String? ->
                println(
                    x
                )
            }
        }
    }

    private fun reportComponent(
        component: ResolvedComponentResult,
        writer: PrintWriter,
        seen: MutableSet<ResolvedComponentResult>,
        filesByIdentifiers: Map<ComponentIdentifier, File>,
        indent: String
    ) {
        writer.print(component.id.displayName)
        val file = filesByIdentifiers[component.id]
        if (file != null) {
            writer.print(" => ")
            writer.print(file.name)
        }
        if (seen.add(component)) {
            writer.println()
            val newIndent = "$indent  "
            for (dependency in component.dependencies) {
                writer.print(newIndent)
                writer.print(dependency.requested.displayName)
                writer.print(" -> ")
                if (dependency is ResolvedDependencyResult) {
                    reportComponent(dependency.selected, writer, seen, filesByIdentifiers, newIndent)
                } else {
                    writer.println(" -> not found")
                }
            }
        } else {
            writer.println(" (already seen)")
        }
    }

    private fun filesByIdentifiers(): Map<ComponentIdentifier, File> {
        val map: MutableMap<ComponentIdentifier, File> = HashMap()
        val identifiers = artifactIdentifiers.get()
        val files: List<File> = ArrayList(artifactFiles.files)
        for (index in identifiers.indices) {
            map[identifiers[index].componentIdentifier] = files[index]
        }
        return map
    }
}

internal abstract class ListResolvedArtifacts : DefaultTask() {
    @get:Input
    abstract val artifactIds: ListProperty<ComponentArtifactIdentifier>

    @get:Input
    abstract val artifactVariants: ListProperty<ResolvedVariantResult>

    @get:InputFiles
    abstract val artifactFiles: ListProperty<RegularFile>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    @Throws(IOException::class)
    fun action() {
        val outputFile = outputFile.asFile.get()
        PrintWriter(FileWriter(outputFile)).use { writer ->
            val ids =
                artifactIds.get()
            val variants =
                artifactVariants.get()
            val files = artifactFiles.get()
            for (index in ids.indices) {
                val id = ids[index]
                val variant = variants[index]
                val file = files[index]
                writer.print("FILE ")
                writer.println(file.asFile.name)
                writer.print("  id: ")
                writer.println(id.displayName)
                writer.print("  variant: ")
                writer.println(variant.displayName)
                writer.print("  size: ")
                writer.println(file.asFile.length())
                writer.println()
            }
        }
        Files.lines(outputFile.toPath()).use { stream ->
            stream.forEach { x: String? ->
                println(
                    x
                )
            }
        }
    }
}