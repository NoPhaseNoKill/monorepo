import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Files

plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.9.23"
}

gradlePlugin {
    plugins {
        create("producerPlugin") {
            id = "com.nophasenokill.producer"
            implementationClass = "com.nophasenokill.ProducerPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

configurations {
    implementation {
        this.isTransitive = false
    }

    implementationDependenciesMetadata {
        this.isTransitive = false
    }

    kotlinCompilerPluginClasspathMain {
        this.isTransitive = false
    }

    kotlinBuildToolsApiClasspath {
        this.isTransitive = false
    }

    kotlinCompilerPluginClasspathTest {
        this.isTransitive = false
    }

    kotlinCompilerClasspath {
        this.isTransitive = false
    }

    kotlinKlibCommonizerClasspath {
        this.isTransitive = false
    }

    compileClasspath {
        this.isTransitive = false
    }

    runtimeClasspath {
        this.isTransitive = false
    }

    testCompileClasspath {
        this.isTransitive = false
    }

    testRuntimeClasspath {
        this.isTransitive = false
    }

    testImplementation {
        this.isTransitive = false
    }

    testImplementationDependenciesMetadata {
        this.isTransitive = false
    }
}

dependencies {
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.23")
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
    kotlinCompilerClasspath("org.jetbrains:annotations:24.1.0")
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-script-runtime:1.9.23")
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-daemon-embeddable:1.9.23")
    kotlinCompilerClasspath("org.jetbrains.intellij.deps:trove4j:1.0.20200330")

    kotlinCompilerPluginClasspathMain("org.jetbrains.kotlin:kotlin-scripting-compiler-impl-embeddable:1.9.23")
    kotlinCompilerPluginClasspathMain("org.jetbrains.kotlin:kotlin-scripting-common:1.9.23")
    kotlinCompilerPluginClasspathMain("org.jetbrains.kotlin:kotlin-scripting-jvm:1.9.23")

    kotlinCompilerPluginClasspathTest("org.jetbrains.kotlin:kotlin-scripting-compiler-impl-embeddable:1.9.23")
    kotlinCompilerPluginClasspathTest("org.jetbrains.kotlin:kotlin-scripting-common:1.9.23")
    kotlinCompilerPluginClasspathTest("org.jetbrains.kotlin:kotlin-scripting-jvm:1.9.23")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2") // may be required in future when using junit params
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.opentest4j:opentest4j:1.3.0")
    testImplementation("org.junit.platform:junit-platform-commons:1.10.2")
    testImplementation("org.junit.platform:junit-platform-engine:1.10.2")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
}

version = "1.0.0-local-dev"
group = "com.nophasenokill"


tasks.test {
    useJUnitPlatform()
}

tasks.register("listDependencies") {
    doLast {
        val uniqueDependencies = mutableSetOf<String>()
        configurations.filter{ it.isCanBeResolved}.forEach { config ->
            try {
                config.resolvedConfiguration.lenientConfiguration.artifacts.forEach { artifact ->
                    val id = "${artifact.moduleVersion.id.group}:${artifact.name}:${artifact.moduleVersion.id.version}"
                    uniqueDependencies.add(id)
                }
            } catch (e: Exception) {
                println("Could not resolve configuration for ${config.name}")
            }
        }
        uniqueDependencies.sorted().forEach { println(it) }
    }
}

tasks.register<ListResolvedArtifacts>("listResolvedArtifacts") {
    val runtimeClasspath: Configuration = configurations.getByName("runtimeClasspath")
    val artifacts =
        runtimeClasspath.incoming.artifacts.resolvedArtifacts

    this.artifactIds.set(artifacts.map(IdExtractor()))


    this.artifactVariants.set(artifacts.map(VariantExtractor()))
    this.artifactFiles.set(artifacts.map(FileExtractor(layout)))
    this.outputFile.set(layout.buildDirectory.file(this.getName() + "/report.txt"))
}

internal class IdExtractor :
    Transformer<List<ComponentArtifactIdentifier>, Collection<ResolvedArtifactResult>> {
    override fun transform(artifacts: Collection<ResolvedArtifactResult>): List<ComponentArtifactIdentifier> {
        return artifacts.map { it.id }.toList()
    }
}

internal class VariantExtractor :
    Transformer<List<ResolvedVariantResult>, Collection<ResolvedArtifactResult>> {
    override fun transform(artifacts: Collection<ResolvedArtifactResult>): List<ResolvedVariantResult> {
        return artifacts.map { it.variant }.toList()
    }
}

internal class FileExtractor(private val projectLayout: ProjectLayout) :
    Transformer<List<RegularFile>, Collection<ResolvedArtifactResult>> {
    override fun transform(artifacts: Collection<ResolvedArtifactResult>): List<RegularFile> {
        val projectDirectory = projectLayout.projectDirectory
        return artifacts.map { projectDirectory.file(it.file.absolutePath) }
    }
}


abstract class ListResolvedArtifacts : DefaultTask() {
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

tasks.register("graphResolvedComponentsAndFiles",
    GraphResolvedComponentsAndFiles::class.java
) {
    val resolvableDependencies =
        configurations.getByName("runtimeClasspath").incoming
    val resolvedArtifacts =
        resolvableDependencies.artifacts.resolvedArtifacts

    this.artifactFiles.from(resolvableDependencies.artifacts.artifactFiles)
    this.artifactIdentifiers.set(resolvedArtifacts.map { result: Set<ResolvedArtifactResult> ->
        result.map { it.id }
    })
    this.rootComponent.set(resolvableDependencies.resolutionResult.rootComponent)
    this.outputFile.set(layout.buildDirectory.file(this.getName() + "/report.txt"))
}

abstract class GraphResolvedComponentsAndFiles : DefaultTask() {
    @get:Input
    abstract val artifactIdentifiers: ListProperty<ComponentArtifactIdentifier>

    @get:InputFiles
    abstract val artifactFiles: ConfigurableFileCollection

    @get:Input
    abstract val rootComponent: Property<ResolvedComponentResult>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    @Throws(IOException::class)
    fun action() {
        val filesByIdentifiers = filesByIdentifiers()
        val outputFile = outputFile.get().asFile
        PrintWriter(outputFile).use { writer ->
            val seen: MutableSet<ResolvedComponentResult> = HashSet()
            reportComponent(rootComponent.get(), writer, seen, filesByIdentifiers, "")
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
        val files: List<File> = artifactFiles.files.toList()
        for (index in identifiers.indices) {
            map[identifiers[index].componentIdentifier] = files[index]
        }
        return map
    }
}
