package com.nophasenokill.tasks

import me.lucko.jarrelocator.JarRelocator
import me.lucko.jarrelocator.Relocation
import org.gradle.api.artifacts.transform.*
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.CompileClasspath
import org.gradle.api.tasks.Input
import java.io.File
import java.util.jar.JarFile
import java.util.stream.Collectors

@CacheableTransform
abstract class ClassRelocatorTask : TransformAction<ClassRelocatorTask.Parameters> {
    interface Parameters : TransformParameters {
        @get:CompileClasspath
        val externalClasspath: ConfigurableFileCollection
        @get:Input
        val excludedPackage: Property<String>
    }

    @get:Classpath
    @get:InputArtifact
    abstract val primaryInput: Provider<FileSystemLocation>

    @get:CompileClasspath
    @get:InputArtifactDependencies
    abstract val dependencies: FileCollection

    override fun transform(outputs: TransformOutputs) {
        val primaryInputFile = primaryInput.get().asFile
        println("Primary input file: $primaryInputFile")
        if (parameters.externalClasspath.contains(primaryInputFile)) {
            outputs.file(primaryInput)
        } else {
            val baseName = primaryInputFile.name.substring(0, primaryInputFile.name.length - 4)
            val outputFile = outputs.file("$baseName-relocated.jar")
            println("Output file: $outputFile")
            relocateJar(primaryInputFile, outputFile)
        }
    }

    private fun relocateJar(input: File, output: File) {
        try {
            val relocatedPackages = (dependencies.flatMap { it.readPackages() } + input.readPackages()).toSet()
            val nonRelocatedPackages = parameters.externalClasspath.flatMap { it.readPackages() }
            val relocations = (relocatedPackages - nonRelocatedPackages).map { packageName ->
                val toPackage = "relocated.$packageName"
                println("$packageName -> $toPackage")
                Relocation(packageName, toPackage)
            }
            println("Relocations: $relocations")

            val jarRelocator = JarRelocator(input, output, relocations)
            println("Starting relocation process...")
            jarRelocator.run()
            println("Relocation process completed.")
        } catch (e: Exception) {
            println("Error during relocation: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    private fun File.readPackages(): Set<String> {
        try {
            JarFile(this).use { jarFile ->
                return jarFile.stream()
                    .filter { !it.isDirectory }
                    .filter { it.name.endsWith(".class") }
                    .map { entry ->
                        entry.name.substringBeforeLast('/').replace('/', '.')
                    }
                    .collect(Collectors.toSet())
            }
        } catch (e: Exception) {
            println("Error reading packages from file $this: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}