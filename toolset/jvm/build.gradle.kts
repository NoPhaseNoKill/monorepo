

val buildPluginsTask = tasks.register("buildPluginsTask") {
    group = "build"
    description = "Build all plugins, which includes assembling them and running all checks (tests/functional tests)"

    val standalonePluginsGradleBuild = gradle.includedBuild("standalone-plugins")
    val standalonePluginOneBuildTask = standalonePluginsGradleBuild.task(":standalone-plugin-one:build")
    val outputDir = file(standalonePluginsGradleBuild.projectDir.path).resolve("build")

    inputs.property("standalone-plugin-one-path", standalonePluginsGradleBuild.projectDir.path)
    outputs.files(outputDir)

    dependsOn(standalonePluginOneBuildTask)
}

val buildApplicationsTask = tasks.register("buildApplicationsTask") {
    group = "build"
    description = "Build all applications, which includes assembling them and running all checks (tests/functional tests)"

    val applicationOneBuild = gradle.includedBuild("applications")
    val applicationOneTask = applicationOneBuild.task(":application-one:build")
    val outputDir = file(applicationOneBuild.projectDir.path).resolve("build")

    inputs.property("application-one-path", applicationOneBuild.projectDir.path)
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
    dependsOn(gradle.includedBuild("standalone-plugins").task(":standalone-plugin-one:build"))
}