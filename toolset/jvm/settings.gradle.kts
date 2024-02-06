
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

    includeBuild("build-logic/plugins")
    repositories.gradlePluginPortal()  // ensures that we have access to our own convention plugins

    // See: https://docs.gradle.org/current/userguide/declaring_repositories.html#ex-enforcing-settings-repositories
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}

plugins {
    id("com.gradle.enterprise") version "3.16.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
    /*
        https://github.com/gradle/common-custom-user-data-gradle-plugin
        Adds some recommendations of tags, links etc from develocity
     */
    id("com.gradle.common-custom-user-data-gradle-plugin") version "1.12.1"
}

rootProject.name = "jvm"


gradleEnterprise {
    buildScan {
        isUploadInBackground = false
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        

        /*
            If you try to abstract these strings to a 'val' outside gradleEnterprise lambda, you get errors
            such as below:

            4 problems were found storing the configuration cache.
            - Gradle runtime: cannot serialize Gradle script object references as these are not supported with the configuration cache.
              See https://docs.gradle.org/8.5/userguide/configuration_cache.html#config_cache:requirements:disallowed_types
                ...
                ...
         */
        obfuscation {
            username { "OBFUSCATED_USERNAME" }
            ipAddresses { addresses -> addresses.map { _ -> "OBFUSCATED_IP_ADDRESS" } }
            hostname { "OBFUSCATED_HOSTNAME" }
        }
    }
}

buildCache {
    local {
        isEnabled = true
        isPush = true
    }
}

val directories = setOf("applications", "libraries")

directories.forEach { moduleCategory ->
    rootDir
        .resolve("modules/${moduleCategory}")
        .listFiles { file -> file.isDirectory && !file.isHidden && !file.name.startsWith("gradle")}
        ?.forEach { projectDirectory: File ->
            /*
                This specifically means each subproject is accessible from :projectName.

                Another benefit is that it only includes the specific leaf node directory
                and NOT any directories in-between the root (where this file is located)
                and the included directory. A HUGE downside of the old approach as default
                behaviour, is that the OBSERVED speed of dependency resolution is significantly
                slower. Currently, I do not quite understand why, but my suspicion is that
                instead of checking for dependency resolution against a single folder (what we want),
                it would previously check against 3, thereby slowing it down by 200% in comparison.

                Taken from the Settings.java docs:

                "As an example, the path a:b adds a project with path :a:b, name b and project
                directory $rootDir/a/b. It also adds the a project with path :a, name a and project
                directory $rootDir/a, if it does not exist already."

                Meaning:

                    include("modules/applications/app")

                    - Adds a project with path ':modules', name 'modules', and project directory '$rootDir/modules'
                    - Adds a project 'modules/applications' with path ':modules:applications', name 'applications', and project directory '$rootDir/modules/applications'
                    - Adds a project 'modules/applications/app' with path ':modules:applications:app', name 'app', and project directory '$rootDir/modules/applications/app'

                The first two of which are completely unnecessary in our use case.

                Please note: IntelliJ currently chooses to display the subsequent folder incorrectly.
                    See: https://youtrack.jetbrains.com/issue/IDEA-82965/Clean-module-names for fix and details
             */
            include(projectDirectory.name)
            project(":${projectDirectory.name}").projectDir = file(projectDirectory.path)
        }
}



gradle.useLogger(CustomEventLogger())

@Suppress("deprecation")
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