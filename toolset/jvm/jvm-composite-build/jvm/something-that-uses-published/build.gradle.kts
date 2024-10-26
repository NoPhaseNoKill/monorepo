import java.util.jar.JarFile

plugins {
    id("java-library")
    kotlin("jvm")
}

group = "com.nophasenokill.kotlin-base.something-that-uses-published"
version = "1.0.local"

tasks.register("dependencyOverview") {
    group = "help"
    description = "Prints inputs and outputs for each configuration in the project and its dependencies."

    val file = file("/home/gardo/projects/monorepo/toolset/jvm/jvm-composite-build/jvm/output.txt")
    doLast {
        println("Project dependency overview:")
        file.writeText("")
        project.configurations.forEach { configuration ->

            file.appendText("\nConfiguration: ${configuration.name}")
            file.appendText("\n--------------------------------------------------")

            // Outputs (Artifacts) for this configuration
            file.appendText("\nOutputs (Artifacts):")
            configuration.artifacts.files.files.forEach { artifactFile ->
                file.appendText(" - ${artifactFile.path}")

                // If it's a JAR, print the classes inside
                if (artifactFile.extension == "jar") {
                    file.appendText("\n   Contents of ${artifactFile.name}:")
                    JarFile(artifactFile).use { jar ->
                        jar.entries().asSequence()
                            .filter { it.name.endsWith(".class") }
                            .forEach { file.appendText("\n     - ${it.name}") }
                    }
                }
            }

            // Inputs (Dependencies) for this configuration
            file.appendText("\nInputs (Dependencies):")
            if (configuration.isCanBeResolved) {
                val resolvedFiles = configuration.resolvedConfiguration.files
                resolvedFiles.forEach { dependencyFile ->
                    file.appendText("\n - ${dependencyFile.path}")

                    // If it's a JAR, print the classes inside
                    if (dependencyFile.extension == "jar") {
                        file.appendText("\n   Contents of ${dependencyFile.name}:")
                        JarFile(dependencyFile).use { jar ->
                            jar.entries().asSequence()
                                .filter { it.name.endsWith(".class") }
                                .forEach { file.appendText("\n     - ${it.name}") }
                        }
                    }
                }
            } else {
                file.appendText("\n - (Not resolvable)")
            }
        }

        file.appendText("\nEnd of dependency overview.")
    }
}

tasks.register("printAllProjectDependencies") {
    group = "Reporting"
    description = "Prints a comprehensive overview of all inputs and outputs for each configuration and task in the project"
    val file = file("/home/gardo/projects/monorepo/toolset/jvm/jvm-composite-build/jvm/output.txt")
    doLast {
        file.writeText("")
        project.configurations.forEach { config ->
            file.appendText("\nConfiguration: ${config.name}")

            // Outputs - Artifacts provided by this configuration
            file.appendText("\n  Outputs (Artifacts):")
            config.artifacts.files.forEach { artifactFile ->
                file.appendText("\n    - ${artifactFile.absolutePath}")
            }

            // Check if the configuration can be resolved
            if (config.isCanBeResolved) {
                // Resolved dependencies (all files in the configuration)
                file.appendText("\n  Inputs (Resolved Dependencies):")
                try {
                    config.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
                        file.appendText("\n    - ${artifact.moduleVersion.id}")
                        file.appendText("\n      * File: ${artifact.file.absolutePath}")
                    }
                } catch (e: Exception) {
                    file.appendText("\n    - (Error resolving dependencies: ${e.message})")
                }
            } else {
                // Non-resolvable configurations: print declared dependencies
                file.appendText("\n  Inputs (Declared Dependencies):")
                config.allDependencies.forEach { dependency ->
                    file.appendText("\n    - ${dependency.group}:${dependency.name}:${dependency.version}")
                }
            }

            // config.hierarchy.forEach {
            //     println("Hierarchy: ${it.incoming.resolutionResult.forEach {  }}")
            // }


            // Additional files attached to this configuration
            // config.files.forEach { file ->
            //     file.appendText("\n    - Additional File: ${file.absolutePath}")
            // }
        }

        file.appendText("\n\nTask Input and Output Overview:")
        tasks.forEach { task ->
            file.appendText("\nTask: ${task.path}")

            // Task Inputs
            file.appendText("\n  Inputs:")
            task.inputs.files.forEach { inputFile ->
                file.appendText("\n    - ${inputFile.absolutePath}")
            }

            // Task Outputs
            file.appendText("\n  Outputs:")
            task.outputs.files.forEach { outputFile ->
                file.appendText("\n    - ${outputFile.absolutePath}")
            }
        }
    }
}

tasks.register("printDependencyGraph") {
    group = "Reporting"
    description = "Prints an overview of the dependency graph, including all components and dependencies."

    doLast {
        val configuration = project.configurations.getByName("runtimeClasspath")
        val resolutionResult = configuration.incoming.resolutionResult

        println("Root Component: ${resolutionResult.root.id}")

        // Print all components (modules/libraries) in the graph
        println("\nAll Components:")
        resolutionResult.allComponents {
            println("  - ${moduleVersion ?: this.id.displayName}")
        }

        // Print all dependencies, including unresolved ones
        println("\nAll Dependencies:")
        resolutionResult.allDependencies {
            when (this) {
                is ResolvedDependencyResult -> {

                    println("  - Resolved: ${this.requested.displayName} -> ${this.selected.id.displayName}")
                    println("       - Resolved variant: ${this.resolvedVariant.displayName} -> ${this.selected.id.displayName}")
                }
                is UnresolvedDependencyResult -> {
                    println("  - Unresolved: ${this.requested.displayName} (${this.failure.message})")
                }
            }
        }

        // Print requested attributes for variant selection
        println("\nRequested Attributes:")
        resolutionResult.requestedAttributes.keySet().forEach { attribute ->
            println("  - ${attribute.name}: ${resolutionResult.requestedAttributes.getAttribute(attribute)}")
        }
    }
}




gradle.taskGraph.whenReady {
    val archiveArtifacts = project(":").configurations.findByName("archives")?.artifacts?.files
    println("Classes in archive artifacts:")

    archiveArtifacts?.files?.forEach { jarFile ->
        if (jarFile.extension == "jar") {
            println("JAR: ${jarFile.name}")
            JarFile(jarFile).use { jar ->
                jar.entries().asSequence().filter { it.name.endsWith(".class") }.forEach { entry ->
                    println(" - ${entry.name}")
                }
            }
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0") {
        isTransitive = false
    }



    implementation(project(path = ":", configuration = "archives"))  {
        isTransitive = false
    }
    implementation(project(path = ":", configuration = "implementationDependencyImplementationConfiguration"))

    testImplementation(project(path = ":", configuration = "testDependencyImplementationConfiguration"))
    testRuntimeOnly(project(path = ":", configuration = "testDependencyRuntimeOnlyConfiguration"))
}

tasks.test {
    useJUnitPlatform()
}
