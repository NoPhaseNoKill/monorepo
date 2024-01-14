package com.nophasenokill

import org.gradle.api.DefaultTask
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * Check that 'dependencies' are defined in alphabetical order and without version.
 */
abstract class DependencyFormatCheck : DefaultTask() {

    @get:Input
    abstract val buildFilePath : Property<String>

    @get:Input
    abstract val declaredDependencies : MapProperty<String, List<String>> // Map of 'scope' to 'coordinates'

    @get:Input
    abstract val declaredConstraints : MapProperty<String, List<String>> // Map of 'scope' to 'coordinates'

    @get:Input
    abstract val shouldNotHaveVersions : Property<Boolean>

    @TaskAction
    fun check() {
        declaredDependencies.get().forEach { (scope, dependencies) ->
            if (shouldNotHaveVersions.get()) {

                dependencies.forEach { coordinates ->
                    if (coordinates.count { it == ':' } == 2 && !coordinates.startsWith("org.jetbrains.kotlin:kotlin-stdlib:")) {
                        throw RuntimeException("""
                            ${buildFilePath.get()}
                            
                            Dependencies with versions are not allowed. Please declare the dependency as follows:
                            
                                ${scope}("${coordinates.substring(0, coordinates.lastIndexOf(':'))}")
                            
                            All versions must be declared in 'build-logic/platforms'.
                            If the version is not yet defined there, add the following to 'build-logic/platforms/build.gradle.kts':
                            
                                api("$coordinates")
                        """.trimIndent())
                    }
                }
            }

            val declaredInBuildFile = dependencies.filter {
                // Ignore dependencies that are defined in commons plugins. Found inside commons-plugin.gradle.kts
                it !in listOf(
                    "com.nophasenokill.platforms:platforms",
                    "org.junit.jupiter:junit-jupiter-engine",
                )
            }
            val sortedProject = declaredInBuildFile.filter { it.startsWith(":") }.sorted()
            val sortedExternal = declaredInBuildFile.filter { !it.startsWith(":") }.sorted()
            if (declaredInBuildFile != sortedProject + sortedExternal) {
                throw RuntimeException("""
                    ${buildFilePath.get()}
                    
                    $scope dependencies are not declared in alphabetical order. Please use this order:
                        ${sortedProject.joinToString("\n                        ") {"${scope}(project(\"${it}\"))"}}
                        ${sortedExternal.joinToString("\n                        ") {"${scope}(\"${it}\")"}}
                """.trimIndent())
            }
        }

        declaredConstraints.get().forEach { (scope, constraints) ->
            val sortedConstraints = constraints.sorted()
            if (constraints != sortedConstraints) {
                throw RuntimeException("""
                    ${buildFilePath.get()}
                    
                    $scope dependency constraints are not declared in alphabetical order. Please use this order:
                        ${sortedConstraints.joinToString("\n                        ") {"${scope}(\"${it}\")"}}
                """.trimIndent())
            }
        }
    }
}
