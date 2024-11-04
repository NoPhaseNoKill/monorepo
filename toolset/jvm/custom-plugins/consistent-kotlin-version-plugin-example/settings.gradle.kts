rootProject.name = "consistent-kotlin-version-plugin-example"

pluginManagement {

    buildscript {
        repositories {
            maven {
                url = uri("https://plugins.gradle.org/m2/")
            }
        }


        configurations.all {
            isTransitive = false

            val configurationName = this.name

            if (configurationName.contains("classpath", ignoreCase = true)) {

                this.resolutionStrategy.setForcedModules()

                val constrained = listOf(
                    "org.jetbrains:annotations:23.0.0",
                    "org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1",
                    "org.jetbrains.kotlin:kotlin-reflect:2.1.0-Beta1",
                    "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0",
                    "org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:5.1.2",
                    "com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1",
                    "com.gradle:develocity-gradle-plugin:3.18.1"
                )
                constrained.forEach {

                    this.resolutionStrategy.force(it)

                    val group = it.split(":")[0]
                    val module = it.split(":")[1]
                    val version = it.split(":")[2]

                    this.resolutionStrategy {
                        eachDependency {

                            if (this.requested.group == "com.gradle.develocity" && this.requested.name == "com.gradle.develocity.gradle.plugin") {
                                this.useTarget("com.gradle:develocity-gradle-plugin:${this.requested.version}")
                            }

                            if (this.requested.group == group && this.requested.name == module) {
                                this.useVersion(version)
                            }
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
            println("Decision to publish build scan was: ${hasFailures}.")
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

gradle.lifecycle.beforeProject {

    project.buildscript {
        repositories {
            gradlePluginPortal()
        }

        dependencies.add("classpath", "org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")
    }


    val project = this

    project.buildscript.configurations.all {
        val configurationName = this.name

        if (configurationName.contains("classpath", ignoreCase = true)) {

            this.resolutionStrategy.setForcedModules()

            val constrained = listOf(
                "org.jetbrains:annotations:23.0.0",
                "org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1",
                "org.jetbrains.kotlin:kotlin-reflect:2.1.0-Beta1",
                "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0",
                "org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:5.1.2",
                "com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1",
                "com.gradle:develocity-gradle-plugin:3.18.1"
            )
            constrained.forEach {

                this.resolutionStrategy.force(it)

                val group = it.split(":")[0]
                val module = it.split(":")[1]
                val version = it.split(":")[2]

                this.resolutionStrategy {
                    eachDependency {

                        if (this.requested.group == "com.gradle.develocity" && this.requested.name == "com.gradle.develocity.gradle.plugin") {
                            this.useTarget("com.gradle:develocity-gradle-plugin:${this.requested.version}")
                        }

                        if (this.requested.group == group && this.requested.name == module) {
                            this.useVersion(version)
                        }
                    }
                }
            }
        }
    }

    project.configurations.all {
        val configurationName = this.name

        if (configurationName.contains("classpath", ignoreCase = true)) {

            this.resolutionStrategy.setForcedModules()

            val constrained = listOf(
                "org.jetbrains:annotations:23.0.0",
                "org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1",
                "org.jetbrains.kotlin:kotlin-reflect:2.1.0-Beta1",
                "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0",
                "org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:5.1.2",
                "com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1",
                "com.gradle:develocity-gradle-plugin:3.18.1"

            )
            constrained.forEach {

                this.resolutionStrategy.force(it)

                val group = it.split(":")[0]
                val module = it.split(":")[1]
                val version = it.split(":")[2]

                this.resolutionStrategy {
                    eachDependency {

                        if (this.requested.group == "com.gradle.develocity" && this.requested.name == "com.gradle.develocity.gradle.plugin") {
                            this.useTarget("com.gradle:develocity-gradle-plugin:${this.requested.version}")
                        }

                        if (this.requested.group == group && this.requested.name == module) {
                            this.useVersion(version)
                        }
                    }
                }
            }
        }
    }
}


gradle.lifecycle.beforeProject {
    val project = this
    project.buildscript.configurations.all {
        val configurationName = this.name

        if (configurationName.contains("classpath", ignoreCase = true)) {

            this.resolutionStrategy.setForcedModules()

            val constrained = listOf(
                "org.jetbrains:annotations:23.0.0",
                "org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1",
                "org.jetbrains.kotlin:kotlin-reflect:2.1.0-Beta1",
                "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0",
                "org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:5.1.2",
                "com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1",
                "com.gradle:develocity-gradle-plugin:3.18.1"

            )
            constrained.forEach {

                this.resolutionStrategy.force(it)

                val group = it.split(":")[0]
                val module = it.split(":")[1]
                val version = it.split(":")[2]

                this.resolutionStrategy {
                    eachDependency {

                        if (this.requested.group == "com.gradle.develocity" && this.requested.name == "com.gradle.develocity.gradle.plugin") {
                            this.useTarget("com.gradle:develocity-gradle-plugin:${this.requested.version}")
                        }

                        if (this.requested.group == group && this.requested.name == module) {
                            this.useVersion(version)
                        }
                    }
                }
            }
        }
    }

    project.configurations.all {
        val configurationName = this.name
        if (configurationName.contains("classpath", ignoreCase = true)) {

            this.resolutionStrategy.setForcedModules()

            val constrained = listOf(
                "org.jetbrains:annotations:23.0.0",
                "org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1",
                "org.jetbrains.kotlin:kotlin-reflect:2.1.0-Beta1",
                "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0",
                "org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:5.1.2",
                "com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1",
                "com.gradle:develocity-gradle-plugin:3.18.1"
            )
            constrained.forEach {

                this.resolutionStrategy.force(it)

                val group = it.split(":")[0]
                val module = it.split(":")[1]
                val version = it.split(":")[2]

                this.resolutionStrategy {
                    eachDependency {

                        if (this.requested.group == "com.gradle.develocity" && this.requested.name == "com.gradle.develocity.gradle.plugin") {
                            this.useTarget("com.gradle:develocity-gradle-plugin:${this.requested.version}")
                        }

                        if (this.requested.group == group && this.requested.name == module) {
                            this.useVersion(version)
                        }
                    }
                }
            }
        }
    }

    tasks.register("gatherProjectDependencies") {

        // Define the output directory
        val outputDir = layout.buildDirectory.dir("custom-tasks/gather-project-dependencies")

        // Define the task output
        outputs.dir(outputDir)

        val projectConfigs = project.configurations.toSet()

        val configurationsToDependencies = projectConfigs.map {
            val sorted = if (it.isCanBeResolved) {
                it.resolve()
                it.incoming.resolutionResult.allDependencies
                    .map { it.requested.displayName }
                    .toSet()
                    .sorted()
            } else {
                null
            }
            it.name to sorted
        }

        doLast {

            val allUniqueDeps = configurationsToDependencies.flatMap {
                val name = it.first
                val dependencies = it.second ?: emptyList()
                val dependenciesOutput = dependencies.joinToString(separator = "\n") { "$name $it" }
                val configFile = outputDir.get().file("configuration-${name}-dependencies.txt").asFile
                configFile.writeText(dependenciesOutput)
                dependencies
            }.toSet()

            val configFile = outputDir.get().file("all-dependencies.txt").asFile

            configFile.writeText("")

            val longestString = allUniqueDeps.maxByOrNull { it.length }.orEmpty()

            allUniqueDeps
                .map { uniqueDep ->
                    val configurationsRelatedToDep = configurationsToDependencies
                        .filter { it.second?.contains(uniqueDep) == true }
                        .map { it.first }
                    val padding = 1
                    val indent = " ".repeat(longestString.length - uniqueDep.length + padding)
                    "$uniqueDep$indent->  Used by $configurationsRelatedToDep"
                }
                .sorted()
                .forEachIndexed { index, uniqueDep ->
                    if (index == 0) configFile.appendText(uniqueDep)
                    else configFile.appendText("\n$uniqueDep")
                }
        }
    }

    tasks.register("gatherBuildScriptDependencies") {

        val outputDir = layout.buildDirectory.dir("custom-tasks/gather-build-script-dependencies/${project.name}")
        outputs.dir(outputDir)

        val configurations = buildscript.configurations.filter { it.isCanBeResolved }
        val configurationsToDependencies = configurations.map {
            val sorted = configurations
                .filter { it.isCanBeResolved }
                .flatMap { config ->
                    config.incoming.resolutionResult.root.dependencies
                        .mapNotNull { dependencyResult ->
                            (dependencyResult as? ResolvedDependencyResult)?.selected?.id?.displayName
                        }
                        .toSet()
                        .sorted()
                }
            it.name to sorted
        }

        val allUniqueDeps = configurationsToDependencies.flatMap {
            val name = it.first
            val dependencies = it.second
            val dependenciesOutput = dependencies.joinToString(separator = "\n") { "$name $it" }
            val configFile = outputDir.get().file("configuration-${name}-dependencies.txt").asFile
            configFile.writeText(dependenciesOutput)
            dependencies
        }.toSet()

        val configFile = outputDir.get().file("all-dependencies.txt").asFile

        doLast {

            configFile.writeText("")

            val longestString = allUniqueDeps.maxByOrNull { it.length }.orEmpty()

            allUniqueDeps
                .map { uniqueDep ->
                    val configurationsRelatedToDep = configurationsToDependencies
                        .filter { it.second.contains(uniqueDep) }
                        .map { it.first }
                    val padding = 1
                    val indent = " ".repeat(longestString.length - uniqueDep.length + padding)
                    "$uniqueDep$indent->  Used by $configurationsRelatedToDep"
                }
                .sorted()
                .forEachIndexed { index, uniqueDep ->
                    if (index == 0) configFile.appendText(uniqueDep)
                    else configFile.appendText("\n$uniqueDep")
                }
        }
    }
}
