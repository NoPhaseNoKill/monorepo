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

            val (nonStbLibDependencies: List<String>, stbLibDependencies: List<String> ) = dependencies.partition { !it.startsWith("org.jetbrains.kotlin:kotlin-stdlib") }

            if (shouldNotHaveVersions.get()) {

                /*
                    Prevent multiple dependency declarations of org.jetbrains.kotlin:kotlin-stdlib
                    This is currently duplicated with platform/project.

                    To replicate: comment out gradle.properties 'kotlin.stdlib.default.dependency=false'
                    and run: ./gradlew checkDependencyFormattingProject


                        $rootDir/modules/applications/app/build.gradle.kts

                        implementation dependencies are not declared in alphabetical order. Please use this order:
                        implementation(project(":list"))
                        implementation(project(":utilities"))
                        implementation("org.apache.commons:commons-text")
                        implementation("org.jetbrains.kotlin:kotlin-stdlib")
                 */

                if(stbLibDependencies.count() > 1) {
                    throw RuntimeException("""
                            ${buildFilePath.get()}
                            
                            Dependency 'org.jetbrains.kotlin:kotlin-stdlib' should only ever be included once, 
                            and instead was included ${stbLibDependencies.count()} times.
                            
                            
                            This may happen when:
                                - You are have set kotlin.stdlib.default.dependency=false inside
                                gradle.properties to true, and it is now implicitly being included
                                an extra time
                                
                                - You are now including a plugin which implicitly includes this
                                
                                - You have added it manually to a plugin
                                
                                Please fix and try again.
                            
                    """.trimIndent())
                }

                nonStbLibDependencies.forEach { coordinates ->

                    if (coordinates.count { it == ':' } == 2 && !coordinates.startsWith("org.jetbrains.kotlin")) {
                        throw RuntimeException("""
                            ${buildFilePath.get()}
                            
                            Dependencies with versions are not allowed. Please declare the dependency as follows:
                            
                                ${scope}("${coordinates.substring(0, coordinates.lastIndexOf(':'))}")
                            
                            All versions must be declared in 'build-logic/platform'.
                            If the version is not yet defined there, add the following to 'build-logic/platform/build.gradle.kts':
                            
                                api("$coordinates")
                        """.trimIndent())
                    }
                }
            }

            val declaredInBuildFile = dependencies.filter {
                // Ignore dependencies that are defined in commons plugins. Found inside base-plugin.gradle.kts
                // This is currently duplicuated with platform/project
                it !in listOf(
                    "com.nophasenokill.platform:platform",
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
