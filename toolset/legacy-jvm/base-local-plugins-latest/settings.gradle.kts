rootProject.name = "declared-substitution"

pluginManagement {
    includeBuild("anonymous-library")
}

include("app")

/*
     This allows us to substitute external modules in the context
     of a project, and point it to our local one. Particularly useful for patching
     dependencies. For instance, the below means that a dependency/import such as:

         import org.sample.numberutils.Numbers;

     Will get resolved to our local build project.

     The reason we need to publish this/have gone to the extent to avoid includeBuild
     is so that we can get full parallelism.
 */

includeBuild("anonymous-library") {
    dependencySubstitution {
        substitute(module("org.sample.number-utils")).using(project(":"))
    }
}

gradle.lifecycle.beforeProject {

    /*
       Repos specifically used for this build.

       Note: This will NOT apply to included builds. In the future, we should
       consider breaking this functionality out, so that we can apply the same
       repos to each included build/sub project.
     */
    project.repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    /*
        Configures custom dependency troubleshooting tasks, and adds
        build, assemble and clean to each subproject.
     */
    plugins.apply("base")

    pluginManager.withPlugin("base") {

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

        project.tasks.named("build") {
            dependsOn("gatherProjectDependencies", "gatherBuildScriptDependencies")
        }
    }
}
