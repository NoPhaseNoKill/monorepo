
group = "com.nophasenokill.jvm"

// runs everything inside of this file
tasks.register("runAllMainBuildTasks") {
    group = mainBuildGroup
    description = "Runs all of the main build tasks, which are checks that you would do manually for each of the sub-projects"
    /*
        This ensures that the clean/build tasks are run initially, so that all build files are retained.
        Without this, we were noticing that the sourceFileHashingPluginTask would result in a build folder
        that didn't contain any of the build files (as if the sourceFileHashingPluginTask had re-created
        the folder)
     */
    mustRunAfter(recompileAllTask)

    dependsOn(checkDependenciesTask)
    dependsOn(detectAllCollisionsTask)
    dependsOn(getAllProjectHealthTask)
    dependsOn(runAllSourceFileHashingTasks)
    dependsOn(testAllTask)
    dependsOn(printAllRuntimeClasspath)
}

// Configure the ':tasks' task of the root project to only show
// the main lifecycle tasks as entry points to the build
val mainBuildGroup = "main build"
tasks.named<TaskReportTask>("tasks") {
    displayGroup = mainBuildGroup
}

val testAllTask = tasks.register("testAll") {
    group = mainBuildGroup
    description = "Runs all of the modules' tests"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:test"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:test"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:test"))

}

val recompileAllTask = tasks.register("recompileAll") {
    group = mainBuildGroup
    description = "Cleans and then re-builds each of the modules' sub projects"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:clean"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:clean"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:clean"))

    dependsOn(gradle.includedBuild("modules").task(":applications:app:build"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:build"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:build"))

}

val checkDependenciesTask = tasks.register("checkDependencies") {
    group = mainBuildGroup
    description = "Checks the formatting of each of the modules' sub-projects"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:checkDependencyFormattingProject"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:checkDependencyFormattingProject"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:checkDependencyFormattingProject"))

}

val runAllSourceFileHashingTasks = tasks.register("runAllSourceFileHashingTasks") {
    group = mainBuildGroup
    description = "Hashes all of the source files for any sub-projects inside of the modules folder"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:sourceFileHashingPluginTask"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:sourceFileHashingPluginTask"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:sourceFileHashingPluginTask"))
}

val printAllRuntimeClasspath = tasks.register("printAllRuntimeClasspath") {
    group = mainBuildGroup
    description = "Prints the run-time classpath of each of the apps inside of the modules folder"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:printRuntimeClasspath"))
}

val detectAllCollisionsTask = tasks.register("detectAllCollisions") {
    group = mainBuildGroup
    description = "Detects potential classpath collisions for any sub-project in the modules folder"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:detectCollisions"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:detectCollisions"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:detectCollisions"))
}

val getAllProjectHealthTask = tasks.register("getAllProjectHealth") {
    group = mainBuildGroup
    description = "Runs dependency analysis for all sub-projects and outputs the project health in the build/reports folder"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:projectHealth"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:projectHealth"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:projectHealth"))
}