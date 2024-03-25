import java.nio.file.Path

/*
    Declare libraries, apps and plugins here
 */
val libraries = setOf(
    ":base:modules:libraries:list:build",
    ":base:modules:libraries:utilities:build",
)

val apps = setOf(
    ":base:modules:applications:app:build",
    ":base:modules:applications:accelerated-test-suite-runner:build",
)

val standalonePlugins = setOf(
    "plugin"
)

tasks.register("buildAll") {
    group = "verification"
    description = "Builds all projects, which includes assembling them and running all checks (tests/functional tests)"

    val standalonePluginsBuildDirs = standalonePlugins.map {
        val plugins = gradle.includedBuild(it)
        val pluginsDir = plugins.projectDir.toPath()

        /*
            This needs to be a relative path to maintain the ability
            to achieve task relocatability.
            See: https://docs.gradle.org/current/userguide/build_cache_debugging.html#caching_relocation_test
         */
        file(pluginsDir).resolve("build").toPath().toString()

    }.toSet()


    val pluginsTaskFilesOutputs = buildPluginsTask.get().outputs.files

    doFirst {
        logger.lifecycle("""
            
            Task ${this.name} is using the outputs from task 'buildPluginsTask' to ensure latest plugins are pulled in. 
                Outputs are any files located inside of $standalonePluginsBuildDirs
        """.trimIndent())
    }

    val combined = libraries + apps

    /*
        Currently assumes that any libraries or applications are always dependent
        on all plugins. This may need to be changed in the future.
     */
    combined.forEach {
        dependsOn(it).dependsOn(pluginsTaskFilesOutputs)
    }
}

/*
    Allows us to have inputs/outputs for all standalone-plugins.
    We can then use the outputs from these, to ensure that they
    are depended upon for all libraries/applications.

    This allows for proper cache invalidation whenever a plugin is
    updated or modified, while still utilizing the full jvm project
    structure.
 */
val buildPluginsTask = tasks.register("buildPluginsTask") {

    standalonePlugins.forEach {

        val info = getStandalonePluginBuildTaskInfo(gradle, it)

        dependsOn(info.first)
        inputs.dir(info.second)
        outputs.dir(info.third)
    }
}

private fun getStandalonePluginBuildTaskInfo(gradle: Gradle, standalonePlugin: String): Triple<TaskReference, Path, File> {
    val plugins = gradle.includedBuild(standalonePlugin)

    val pluginBuildTask = plugins.task(":build")
    val pluginsDir = plugins.projectDir.toPath()
    val pluginsFiles = file(pluginsDir).resolve("build")

    return Triple(pluginBuildTask, pluginsDir, pluginsFiles)
}