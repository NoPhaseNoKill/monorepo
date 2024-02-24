plugins {
    /*
        Plugin that outputs performance metrics of the gradle command that was run to the $rootDir/build
        directory. This can be useful in diagnosing bottlenecks, long-running tasks, or simply
        to get an idea of the order in which tasks are run.

        The intended long-term plan of this is to get a local equivalent of the build scan, which can then be used
        to store information in a database for better observations of build improvements or degradation.
     */
    id("performance-metrics-plugin")
}

group = "com.nophasenokill.jvm"

/*

    This file contains primarily two tasks you should run:
        - runAll or;
        - cleanRunAll

    Alternatively, run ./gradlew tasks for a full list of exposed tasks. Other
    useful tasks are exposed, or available directly through the normal :path:to:task.

    The only difference between runAll and cleanRunAll are that the second one
    ensures it cleans everything first, and that the clean task for each of the projects runs before anything else does.
*/

/*
    Configure the ':tasks' task of the root project to only show
    the main lifecycle tasks as entry points to the build
 */
val mainBuildGroup = "main build"
tasks.named<TaskReportTask>("tasks") {
    displayGroup = mainBuildGroup
}

val runAll = tasks.register("runAll") {
    group = mainBuildGroup
    description = "Runs all of the main build sub-tasks"

    dependsOn(subTaskCheckDependenciesAll)
    dependsOn(subTaskDetectClasspathCollisionsAll)
    dependsOn(subTaskProjectHealthAll)
    dependsOn(subTaskSourceFileHashingRunAll)
    dependsOn(subTaskTestAll)
    dependsOn(subTaskPrintRuntimeClasspathAll)
}


tasks.register("cleanRunAll") {
    group = mainBuildGroup
    description = "Cleans everything first, and then runs all of the main build sub-tasks"
    /*
        This ensures that the clean/build tasks are run initially, so that all build files are retained.
        Without this, we were noticing that the runAllSourceFileHashingTasks would result in a build folder
        that didn't contain any of the build files (as if the runAllSourceFileHashingTasks had re-created
        the folder)
     */
    dependsOn(subTaskCleanAll)
    finalizedBy(runAll)
}


val subTaskTestAll = tasks.register("subTaskTestAll") {
    group = mainBuildGroup
    description = "Runs all of the modules' tests"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:test"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:test"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:test"))

}

val subTaskCleanAll = tasks.register("subTaskCleanAll") {
    group = mainBuildGroup
    description = "Cleans each of the modules' sub projects"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:clean"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:clean"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:clean"))
}

val subTaskCheckDependenciesAll = tasks.register("subTaskCheckDependenciesAll") {
    group = mainBuildGroup
    description = "Checks the formatting of each of the modules' sub-projects"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:checkDependencyFormattingProject"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:checkDependencyFormattingProject"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:checkDependencyFormattingProject"))

}

val subTaskSourceFileHashingRunAll = tasks.register("subTaskSourceFileHashingRunAll") {
    group = mainBuildGroup
    description = "Hashes all of the source files for any sub-projects inside of the modules folder"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:sourceFileHashingPluginTask"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:sourceFileHashingPluginTask"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:sourceFileHashingPluginTask"))
}

val subTaskPrintRuntimeClasspathAll = tasks.register("subTaskPrintRuntimeClasspathAll") {
    group = mainBuildGroup
    description = "Prints the run-time classpath of each of the apps inside of the modules folder"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:printRuntimeClasspath"))
}

val subTaskDetectClasspathCollisionsAll = tasks.register("subTaskDetectClasspathCollisionsAll") {
    group = mainBuildGroup
    description = "Detects classpath collisions for any sub-project in the modules folder"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:detectClasspathCollisions"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:detectClasspathCollisions"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:detectClasspathCollisions"))
}

val subTaskProjectHealthAll = tasks.register("subTaskProjectHealthAll") {
    group = mainBuildGroup
    description = "Runs dependency analysis for all sub-projects and outputs the project health in the build/reports folder"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:projectHealth"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:projectHealth"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:projectHealth"))
}