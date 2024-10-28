package com.nophasenokill

// Using star import to workaround https://youtrack.jetbrains.com/issue/KTIJ-24390
import com.gradle.develocity.agent.gradle.scan.BuildScanConfiguration
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Provider
import org.gradle.build.event.BuildEventsListenerRegistry
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.support.serviceOf

/**
 * In build-logic and main build, register a BuildService instance separately,
 * which collects necessary information for build scan.
 */
fun <T : AbstractBuildScanInfoCollectingService> Project.registerBuildScanInfoCollectingService(
    /* the implementation class to collect information from task execution result */
    klass: Class<T>,
    /* which tasks we need to monitor? For example, cache-miss-monitor monitors `AbstractCompile` tasks */
    taskFilter: (Task) -> Boolean,
    /* pass the collected information in build-logic and main build to build scan */
    buildScanAction: BuildScanConfiguration.(Any, Any) -> Unit
) {
    val gradleRootProject = when {
        project.name == "gradle" -> project.rootProject
        project.rootProject.name == "build-logic" -> rootProject.gradle.parent?.rootProject
        else -> project.gradle.parent?.rootProject
    }

    if (gradleRootProject != null && System.getenv("TEAMCITY_VERSION") != null) {
        val rootProjectName = rootProject.name
        val isInBuildLogic = rootProjectName == "build-logic"
        gradle.taskGraph.whenReady {
            val buildService: Provider<T> = gradle.sharedServices.registerIfAbsent("${klass.simpleName}-$rootProjectName", klass) {
                parameters.monitoredTaskPaths = allTasks.filter(taskFilter).map { if (isInBuildLogic) ":build-logic${it.path}" else it.path }.toSet()
            }

            gradle.serviceOf<BuildEventsListenerRegistry>().onTaskCompletion(buildService)
            gradleRootProject.extensions.extraProperties.set("collectedInfo-${klass.simpleName}-${rootProjectName}", buildService.get().collectedInformation)

            if (!isInBuildLogic) { // BuildScanExtension is only available in the gradle project
                val buildScan = gradleRootProject.extensions.findByType<BuildScanConfiguration>()
                val infoCollectedInBuildLogic = gradleRootProject.extensions.extraProperties.get("collectedInfo-${klass.simpleName}-build-logic")!!
                val infoCollectedInMainBuild = gradleRootProject.extensions.extraProperties.get("collectedInfo-${klass.simpleName}-gradle")!!
                buildScan?.buildScanAction(infoCollectedInBuildLogic, infoCollectedInMainBuild)
            }
        }
    }
}
