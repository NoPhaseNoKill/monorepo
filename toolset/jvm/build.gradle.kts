plugins {
    `kotlin-dsl` version("4.3.0") apply false
}

val buildPluginsTask = tasks.register("buildPluginsTask") {
    group = "build"
    description = "Build all plugins, which includes assembling them and running all checks (tests/functional tests)"

    val standalonePluginsGradleBuild = gradle.includedBuild("standalone-plugins")

    val standalonePlugins = setOf(
        "standalone-plugin-one"
    )

    standalonePlugins.forEach {
        configureTaskAndOutput(standalonePluginsGradleBuild, it)
    }
}

val buildApplicationsTask = tasks.register("buildApplicationsTask") {
    group = "build"
    description = "Build all applications, which includes assembling them and running all checks (tests/functional tests)"

    val applicationOneBuild = gradle.includedBuild("applications")
    val apps = setOf(
        "application-one"
    )
    apps.forEach {
        configureTaskAndOutput(applicationOneBuild, it)
    }
}

fun Task.configureTaskAndOutput(includedBuild: IncludedBuild, subProjectName: String) {
    val applicationOneTask = includedBuild.task(":${subProjectName}:build")
    val outputDir = file(includedBuild.projectDir.path).resolve("build")

    inputs.property("${subProjectName}-path", includedBuild.projectDir.path)
    outputs.files(outputDir)

    dependsOn(applicationOneTask)
}

tasks.register("buildAllComposite") {
    group = "verification"
    description = "Builds all projects, which includes assembling them and running all checks (tests/functional tests)"

    inputs.files(buildPluginsTask.get().inputs.files)
    inputs.files(buildApplicationsTask.get().inputs.files)

    outputs.files(buildPluginsTask.get().outputs.files)
    outputs.files(buildApplicationsTask.get().outputs.files)

    dependsOn(gradle.includedBuild("applications").task(":application-one:build"))

    /*
        This ensures that even though the JAR is a dependency for an app/lib, that the tests
        for the plugins that produce said JAR, are also run as a part of this task. This allows
        for:

            1. Not having to wait for functional tests to pass before beginning any app/lib tasks
            2. Only has to wait until JAR is produced before app/lib tasks can run
            3. Runs all the plugin checks in parallel to the app/lib tasks

        This ideally represents a configuration - ie JAR produced from plugin allows compilation
        of the app/lib, but the plugin's build task result is required for us to build the whole
        composite and confirm we haven't borked anything. We should look at changing this at a later date.
     */

    dependsOn(
        gradle.includedBuild("meta-plugins").task(":meta-plugin-one:build")
        // gradle.includedBuild("standalone-plugins").task(":standalone-plugin-one:build")
    ).dependsOn(
        gradle.includedBuild("standalone-plugins").task(":standalone-plugin-one:build")
        // gradle.includedBuild("meta-plugins").task(":build")
    )
    //
    // dependsOn(
    //     gradle.includedBuild("standalone-plugins").task(":standalone-plugin-one:build")
    //     // gradle.includedBuild("meta-plugins").task(":build")
    // )
}