rootProject.name = "consistent-kotlin-version-plugin-example"


pluginManagement {

    repositories {
        gradlePluginPortal()
    }

    plugins {
        id("org.jetbrains.kotlin.jvm") version "2.1.0-Beta1"
        id("com.gradle.develocity") version "3.18.1"
    }

    buildscript {

        repositories {
            gradlePluginPortal()
        }

        configurations.all {
            this.isTransitive = false

            if(this.name.contains("classpath")) {
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-idea:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-tooling-core:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-annotations:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-idea-proto:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-klib-commonizer-api:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-native-utils:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-util-io:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-util-klib:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-build-tools-api:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-build-statistics:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "com.google.code.gson:gson:2.11.0")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-util-klib-metadata:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugins-bom:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-model:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-compiler-runner:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-daemon-client:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.1.0-Beta1")
                buildscript.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")

                /*
                   These do not require constraints, they simply require different resolution results. See below resolutionStrategy
                   for details.

                    buildscript.dependencies.constraints.add(this.name, "com.gradle:develocity-gradle-plugin:3.18.1")
                    buildscript.dependencies.constraints.add(this.name, "com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1")

                 */
            }

            /*
                Equivalent of:
                    buildscript.configurations.all {
                       resolutionStrategy {

                       }
                    }
             */

            this.resolutionStrategy {

                val forcedModules = listOf("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")

                forcedModules.forEach {

                    setForcedModules() // Clears any forced modules from gradle (aka gradle version being dependent on an old stdlib version)
                    eachDependency {

                        val group = it.split(":")[0]
                        val module = it.split(":")[1]
                        val version = it.split(":")[2]


                        /*
                            Prevents dependency graph showing two dependencies when really it's just one that's being resolved differently.

                            Without this we would get either:

                            1.

                            Could not apply requested plugin [id: 'com.gradle.develocity', version: '3.18.1'] as it does not provide a plugin with id 'com.gradle.develocity'. This is caused by an incorrect plugin implementation. Please contact the plugin author(s).
                                > Plugin with id 'com.gradle.develocity' not found

                                OR

                            2.

                            com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1 -> com.gradle:develocity-gradle-plugin:3.18.1

                                OR
                            3.

                            com.gradle:develocity-gradle-plugin:3.18.1 constraint
                            com.gradle:develocity-gradle-plugin:{prefer 3.17.6} -> 3.18.1

                            OR

                            A silent failure, because the resolution failed.

                            In the future, this should be done as a capability instead.

                         */

                        if (this.requested.group == "com.gradle.develocity" && this.requested.name == "com.gradle.develocity.gradle.plugin") {
                            this.useTarget("com.gradle:develocity-gradle-plugin:${this.requested.version}")
                        }

                        // Aligns the versions to what we want
                        if (this.requested.group == group && this.requested.name == module) {
                            this.useVersion(version)
                        }

                        force(it)
                    }
                }
            }
        }

        buildscript.dependencies.classpath("com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1") {
            isTransitive = false
        }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("com.gradle.develocity")
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

gradle.lifecycle.beforeProject {

    val project = this

   project.buildscript {
       project.buildscript.repositories {
           project.buildscript.repositories.maven {
                url = uri("https://plugins.gradle.org/m2/")
            }
        }
       project.buildscript.dependencies.add("classpath", "org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.1.0-Beta1") { isTransitive = false }

       project.buildscript.configurations.all {
           isTransitive = false
           this.resolutionStrategy.setForcedModules() // Clears any forced modules from gradle (aka gradle version being dependent on an old stdlib version)

           if(this.name.contains("classpath")) {

               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-idea:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-tooling-core:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-annotations:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-idea-proto:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-klib-commonizer-api:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-native-utils:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-util-io:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-util-klib:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-build-tools-api:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-build-statistics:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "com.google.code.gson:gson:2.11.0") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-util-klib-metadata:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-model:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-compiler-runner:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-daemon-client:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.1.0-Beta1") { isTransitive = false }
               project.buildscript.dependencies.add(this.name, "org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1") { isTransitive = false }

               /*
                These do not need a dependency as they are applied at the settings level:
                    project.buildscript.dependencies.add(this.name, "com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1") { isTransitive = false }
                */

           }

           /*
            Equivalent of:
                project.buildscript.configurations.all {
                   resolutionStrategy {

                   }
                }

            NOT to be confused with:

             project.configurations.all {
                resolutionStrategy {

                }
             }
         */

           this.resolutionStrategy {

               val forcedModules = listOf("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")

               forcedModules.forEach {

                   setForcedModules() // Clears any forced modules from gradle (aka gradle version being dependent on an old stdlib version)
                   eachDependency {

                       val group = it.split(":")[0]
                       val module = it.split(":")[1]
                       val version = it.split(":")[2]


                       /*
                           Prevents dependency graph showing two dependencies when really it's just one that's being resolved differently.

                           Without this we would get either:

                           1.

                           Could not apply requested plugin [id: 'com.gradle.develocity', version: '3.18.1'] as it does not provide a plugin with id 'com.gradle.develocity'. This is caused by an incorrect plugin implementation. Please contact the plugin author(s).
                               > Plugin with id 'com.gradle.develocity' not found

                               OR

                           2.

                           com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1 -> com.gradle:develocity-gradle-plugin:3.18.1

                               OR
                           3.

                           com.gradle:develocity-gradle-plugin:3.18.1 constraint
                           com.gradle:develocity-gradle-plugin:{prefer 3.17.6} -> 3.18.1

                           OR

                           A silent failure, because the resolution failed.

                           In the future, this should be done as a capability instead.

                        */

                       if (this.requested.group == "com.gradle.develocity" && this.requested.name == "com.gradle.develocity.gradle.plugin") {
                           this.useTarget("com.gradle:develocity-gradle-plugin:${this.requested.version}")
                       }

                       // Aligns the versions to what we want
                       if (this.requested.group == group && this.requested.name == module) {
                           this.useVersion(version)
                       }

                       force(it)
                   }
               }
           }
       }
    }

    project.configurations.all {
        isTransitive = false

        val configuration = this

        assert(configuration is Configuration)

        println("Can be resovled: ${this.isCanBeResolved} for ${this.name}")
        println("Can be consumed: ${this.isCanBeConsumed} for ${this.name}")

        if(this.isCanBeResolved && !this.name.contains("classpath", true)) {
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-idea:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-tooling-core:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-annotations:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-idea-proto:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-klib-commonizer-api:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-native-utils:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-util-io:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-util-klib:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-build-tools-api:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-build-statistics:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "com.google.code.gson:gson:2.11.0") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-util-klib-metadata:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-gradle-plugin-model:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-compiler-runner:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-daemon-client:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.1.0-Beta1") { isTransitive = false }
            project.dependencies.constraints.add(this.name, "org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1") { isTransitive = false }

            /*
             These do not need a dependency as they are applied at the settings level:

                 project.dependencies.constraints.add(this.name, "com.gradle:develocity-gradle-plugin:3.18.1") { isTransitive = false }
             */
        }


        /*
         Equivalent of:
             project.configurations.all {
                resolutionStrategy {

                }
             }

         NOT to be confused with:

             project.buildscript.configurations.all {
                   resolutionStrategy {

                   }
                }
        */

        this.resolutionStrategy {

            val forcedModules = listOf("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")

            forcedModules.forEach {

                setForcedModules() // Clears any forced modules from gradle (aka gradle version being dependent on an old stdlib version)
                eachDependency {

                    val group = it.split(":")[0]
                    val module = it.split(":")[1]
                    val version = it.split(":")[2]


                    /*
                        Prevents dependency graph showing two dependencies when really it's just one that's being resolved differently.

                        Without this we would get either:

                        1.

                        Could not apply requested plugin [id: 'com.gradle.develocity', version: '3.18.1'] as it does not provide a plugin with id 'com.gradle.develocity'. This is caused by an incorrect plugin implementation. Please contact the plugin author(s).
                            > Plugin with id 'com.gradle.develocity' not found

                            OR

                        2.

                        com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1 -> com.gradle:develocity-gradle-plugin:3.18.1

                            OR
                        3.

                        com.gradle:develocity-gradle-plugin:3.18.1 constraint
                        com.gradle:develocity-gradle-plugin:{prefer 3.17.6} -> 3.18.1

                        OR

                        A silent failure, because the resolution failed.

                        In the future, this should be done as a capability instead.

                     */

                    if (this.requested.group == "com.gradle.develocity" && this.requested.name == "com.gradle.develocity.gradle.plugin") {
                        this.useTarget("com.gradle:develocity-gradle-plugin:${this.requested.version}")
                    }

                    // Aligns the versions to what we want
                    if (this.requested.group == group && this.requested.name == module) {
                        this.useVersion(version)
                    }

                    force(it)
                }
            }
        }

    }

    tasks.register("gatherProjectDependencies") {

        val outputDir = layout.buildDirectory.dir("custom-tasks/gather-project-dependencies")


        outputs.dir(outputDir)
        outputDir.get().asFile.mkdirs()

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
                configFile.createNewFile()
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

        outputDir.get().asFile.mkdirs()

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
            outputDir.get().asFile.mkdirs()
            val configFile = outputDir.get().file("configuration-${name}-dependencies.txt").asFile
            configFile.createNewFile()
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

