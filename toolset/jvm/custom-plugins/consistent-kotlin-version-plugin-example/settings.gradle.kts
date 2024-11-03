pluginManagement {
    buildscript {
        repositories {
            maven {
                url = uri("https://plugins.gradle.org/m2/")
            }
        }

        configurations.all {
            val configurationName = this.name
            if (configurationName.contains("classpath", ignoreCase = true)) {
                this.resolutionStrategy.setForcedModules()
                val constrained = listOf(
                    "org.jetbrains:annotations:23.0.0",
                    "org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1",
                    "org.jetbrains.kotlin:kotlin-reflect:2.1.0-Beta1",
                    "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0",
                    "org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:5.1.2",
                    "com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1"
                )
                constrained.forEach {
                    this.resolutionStrategy.force(it)
                    val (group, module, version) = it.split(":")
                    this.resolutionStrategy.eachDependency {
                        if (this.requested.group == group && this.requested.name == module) {
                            this.useVersion(version)
                        }
                    }
                }
            }
        }

        dependencies {
            classpath("com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1")
        }
    }
}

dependencyResolutionManagement {
    rulesMode.set(RulesMode.FAIL_ON_PROJECT_RULES)
    repositories.mavenCentral()
}

plugins {
    id("com.gradle.develocity") version ("3.18.1")
}

develocity {
    buildScan {
        publishing.onlyIf {
            val hasFailures = it.buildResult.failures.isNotEmpty()
            println("Decision to publish build scan was: $hasFailures.")
            if (hasFailures) {
                println("Failures were: ${it.buildResult.failures}")
            }
            hasFailures
        }
        uploadInBackground.set(false)
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")

        obfuscation {
            username { "OBFUSCATED_USERNAME" }
            ipAddresses { addresses -> addresses.map { _ -> "OBFUSCATED_HOSTNAME" } }
            hostname { "OBFUSCATED_IP_ADDRESS" }
        }
    }
}

include("app")
include("module")

gradle.lifecycle.afterProject {
    val constrained = listOf(
        "org.jetbrains:annotations:23.0.0",
        "org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1",
        "org.jetbrains.kotlin:kotlin-reflect:2.1.0-Beta1",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0",
        "org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:5.1.2",
        "com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1"
    )

    project.buildscript.configurations.all {
        if (name.contains("classpath", ignoreCase = true)) {
            resolutionStrategy.setForcedModules()
            constrained.forEach {
                resolutionStrategy.force(it)
                val (group, module, version) = it.split(":")
                resolutionStrategy.eachDependency {
                    if (requested.group == group && requested.name == module) {
                        useVersion(version)
                    }
                }
            }
        }
    }

    abstract class GatherDependenciesTask : DefaultTask() {

        @get:OutputDirectory
        abstract val outputDir: DirectoryProperty

        @get:Input
        abstract val dependencyInfo: ListProperty<Pair<String, List<String>>>

        @TaskAction
        fun gatherDependencies() {
            val outputDirFile = outputDir.get().asFile
            outputDirFile.mkdirs()

            val allUniqueDeps = dependencyInfo.get().flatMap { it.second }.toSet()
            val longestString = allUniqueDeps.maxByOrNull { it.length } ?: ""

            val dependenciesOutput = allUniqueDeps.joinToString("\n") { dep ->
                val configurationsUsingDep = dependencyInfo.get().filter { it.second.contains(dep) }.map { it.first }
                "$dep${" ".repeat(longestString.length - dep.length + 1)}-> Used by [$configurationsUsingDep]"
            }

            outputDirFile.resolve("all-dependencies.txt").writeText(dependenciesOutput)
        }
    }


    tasks.register("gatherProjectDependencies", GatherDependenciesTask::class.java) {
        outputDir.set(layout.buildDirectory.dir("custom-tasks/gather-project-dependencies/${project.name}"))
        dependencyInfo.set(project.provider {
            project.configurations.mapNotNull { config ->
                if (config.isCanBeResolved) {
                    val dependencies = config.incoming.resolutionResult.allDependencies
                        .mapNotNull { dep -> (dep.requested as? ModuleComponentSelector)?.displayName }
                        .sorted()
                    config.name to dependencies
                } else {
                    null
                }
            }
        })
    }

    tasks.register("gatherBuildScriptDependencies", GatherDependenciesTask::class.java) {
        outputDir.set(layout.buildDirectory.dir("custom-tasks/gather-build-script-dependencies/${project.name}"))
        dependencyInfo.set(project.provider {
            project.buildscript.configurations.mapNotNull { config ->
                if (config.isCanBeResolved) {
                    val dependencies = config.incoming.resolutionResult.allDependencies
                        .mapNotNull { dep -> (dep.requested as? ModuleComponentSelector)?.displayName }
                        .sorted()
                    config.name to dependencies
                } else {
                    null
                }
            }
        })
    }
}
