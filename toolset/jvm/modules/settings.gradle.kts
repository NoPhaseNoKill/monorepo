
/*
    By explicitly using exclusive content, regardless of what is defined first,
    we have the ability to say that a particular repository will contain
    everything except a particular group. This can be used to increase
    performance, where we know that precompiled scripts/binary plugins
    for our project should only reference the local version.

    For example:

        repositories {
        // This repository will NOT be searched for artifacts in my.company
        // despite being declared first
        gradlePluginPortal()
        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://repo.mycompany.com/maven2")
                }
            }
            filter {
                // this repository ONLY contains artifacts with group "com.nophasenokill"
                includeGroup("com.nophasenokill")
            }
        }
    }
 */
pluginManagement {
    repositories {
        gradlePluginPortal()
        exclusiveContent {
            val repos = listOf<ArtifactRepository>(mavenCentral(), mavenLocal())
            forRepositories(*repos.toSet().toTypedArray())
            filter {
                includeGroup("com.nophasenokill")
            }
        }
    }
}



dependencyResolutionManagement {

    includeBuild("../build-logic/plugins")
    includeBuild("../build-logic/platform")
    repositories.gradlePluginPortal()  // ensures that we have access to our own convention plugins

    // See: https://docs.gradle.org/current/userguide/declaring_repositories.html#ex-enforcing-settings-repositories
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}



rootProject.name = "modules"

include(":libraries:list")
include(":libraries:utilities")
include(":applications:app")

gradle.useLogger(CustomEventLogger())

class CustomEventLogger : BuildAdapter(), ProjectEvaluationListener, TaskExecutionGraphListener, TaskExecutionListener {

    /*
        These are both Ways to get it to output semi-nicely in terminal, basically something like:

        > Configure project :utilities
            ...
        Graph populated with tasks: ...
             ----        This signals the end of the CONFIGURATION phase     ----

             ----        This signals the start of the EXECUTION stage     ----

        > Task :list:checkKotlinGradlePluginConfigurationErrors
              |------> about to be executed
              |------> was executed

     */
    private val TERMINAL_INDENT = "      "
    private val TERMINAL_PHASE_PRE_FIX = "${TERMINAL_INDENT}#### - "
    private val TERMINAL_PHASE_POST_FIX = " - ####"
    private val EVENT_PRE_FIX = "[EVENT]"
    private val PHASE_PRE_FIX = "[PHASE]"
    private val taskStartTimes: MutableMap<String, Long> = mutableMapOf()
    private val taskDurationTimes: MutableMap<String, Long> = mutableMapOf()
    private val taskEndTimes: MutableMap<String, Long> = mutableMapOf()
    private var buildStartTime: Long = 0L
    private var buildEndTime: Long = 0L

    enum class Phase {
        INITIALIZATION,
        CONFIGURATION,
        EXECUTION
    }

    enum class PhaseStage {
        START,
        END
    }

    override fun beforeSettings(settings: Settings) {
        printPhase(Phase.INITIALIZATION, PhaseStage.START)
    }

    override fun settingsEvaluated(settings: Settings) {
        printPhase(Phase.INITIALIZATION, PhaseStage.END)
        printPhase(Phase.CONFIGURATION, PhaseStage.START)
    }

    override fun beforeEvaluate(project: Project) {
        printEvent("Before evaluate project: ${project.name}")
    }

    override fun afterEvaluate(project: Project, state: ProjectState) {
        printEvent("After evaluate project: ${project.name}. Project was evaluated: ${state.executed}.")
        if(state.failure != null) {
            println("After evaluate failure: ${(state.failure as Throwable).printStackTrace()}")
        }
    }

    override fun beforeExecute(task: Task) {
        println("${TERMINAL_INDENT}|------> about to be executed")
        if(taskStartTimes.isEmpty()) {
            buildStartTime = System.nanoTime()
        }
        taskStartTimes[task.path] = System.nanoTime()
    }

    override fun afterExecute(task: Task, state: TaskState) {
        println("${TERMINAL_INDENT}|------> was executed")
        val startTime = taskStartTimes[task.path] ?: return
        val endTime = System.nanoTime()
        taskEndTimes[task.path] = endTime
        taskDurationTimes[task.path] = endTime - startTime
    }


    override fun graphPopulated(graph: TaskExecutionGraph) {
        printEvent("Graph populated with tasks: ${graph.allTasks.joinToString { it.name }}")
        printPhase(Phase.CONFIGURATION, PhaseStage.END)
        printPhase(Phase.EXECUTION, PhaseStage.START)
    }

    @Deprecated("Deprecated in Java")
    override fun buildFinished(result: BuildResult) {

        printEvent("Build completed")
        printPhase(Phase.EXECUTION, PhaseStage.END)
        if (result.failure != null) {
            (result.failure as Throwable).printStackTrace()
        }
        buildEndTime = System.nanoTime()
        printSummary()
    }

    /*
        Should look something like:

            [Build summary]
                  |
                  | checkKotlinGradlePluginConfigurationErrors took 154400 nanoseconds
                  | processResources took 160039 nanoseconds
                  | processTestResources took 126741 nanoseconds
                  | compileKotlin took 831487 nanoseconds
                  | compileJava took 187275 nanoseconds
                  | classes took 17677 nanoseconds
                  | jar took 345801 nanoseconds
                  | compileTestKotlin took 1192262 nanoseconds
                  | compileTestJava took 245566 nanoseconds
                  | testClasses took 18962 nanoseconds
                  | test took 586181 nanoseconds
                  | testAll took 15874 nanoseconds
                  -----------------------------------------------------------------
                   Total task time taken (wall-clock): 8.6 ms (8641817 nanoseconds)
                  -----------------------------------------------------------------
     */

    private fun printSummary() {
        println()
        println("[Build summary]")
        val linePrefix = "${TERMINAL_INDENT}|"
        println(linePrefix)

        taskDurationTimes.forEach { (task, duration) ->
            println("${linePrefix} ${task} took ${duration} nanoseconds")
        }

        val milliseconds: Double = (buildEndTime - buildStartTime) / 1_000_000.0 // Convert to double for floating-point division
        val formattedMilliseconds = String.format("%.1f", milliseconds) // Formats to 1 decimal place

        val wallClockMessage = "${TERMINAL_INDENT} Total task time taken (wall-clock): ${formattedMilliseconds} ms (${buildEndTime - buildStartTime} nanoseconds)"
        val separator = "-".repeat(wallClockMessage.length - TERMINAL_INDENT.length)

        println(TERMINAL_INDENT + separator)
        println(wallClockMessage)
        println(TERMINAL_INDENT + separator)
        println()
    }

    /*

        For the start, you get something like:

        ---------------------------------------------------------------------
              #### - This signals the START of the CONFIGURATION phase - ####

        > Configure project :

                [EVENT] Before evaluate project: jvm



        For the end, you get something like:

              [EVENT] Build completed

              #### - This signals the END of the EXECUTION phase - ####
        ---------------------------------------------------------------

     */

    private fun printPhase(phase: Phase, stage: PhaseStage) {
        val message = "${TERMINAL_PHASE_PRE_FIX}This signals the ${stage} of the ${phase} phase${TERMINAL_PHASE_POST_FIX}"
        val width = message.length
        val containerMessage = "-".repeat(width)

        println()

        if(stage.equals(PhaseStage.START)) {
            println(containerMessage)
        }

        println(message)

        if(stage.equals(PhaseStage.END)) {
            println(containerMessage)
        }

    }

    private fun printEvent(event: String) {
        println()
        println("${TERMINAL_INDENT}${EVENT_PRE_FIX} ${event}")
    }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")