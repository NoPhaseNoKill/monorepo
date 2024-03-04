package com.nophasenokill.find

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskCollection

/*
   This provides a standardised set of API's for working inside ny gradle build scripts.
   It will not guarantee they are being used in conjunction with each-other correctly,
   which may result in configuration time being spent unnecessarily on particular things,
   but is merely the latest set of known ways to interact with gradle.

   The eventual goal of this is that you won't actually need to interact with the horrendously-aged
   gradle API functions. These should make sense to users, offer a single entry point to do one thing
   (not 50) and severely limits the way you will think about this gradle. You will learn that this is
   a good thing, but depending on your gradle experience, may feel limiting at first.


 */
class ConfigurationTimeSafeAccessors {
    companion object {

        /*
            Find all tasks on the task graph by name
         */
        fun Project.findTasksByName(name: String): TaskCollection<Task> {
            return this.tasks.matching { it.name.contains(name) }
        }

        /*
            Configure details of all matching tasks that are part of the task graph.

            This is particularly useful for looking up an existing plugin's task,
            getting all instances of it, and adding inputs/outputs to make it lifecycle
            aware.
         */
        fun Project.findAndConfigureTasksByName(name: String, block: () -> Unit) {
            this.findTasksByName(name).configureEach {
                block()
            }
        }

        /*
            Creates a task output on a file with a specified output.
            This is useful for creating ad-hoc outputs, where you may
            want to help make a task lifecycle aware or avoid task execution
            when its up to date.
         */

        fun Project.createTaskUpToDateCheck(
            task: Task,
            onSuccessFileText: String,
            onFailureFileText: String,
        ) {
            val outputDir = this.layout.buildDirectory.dir("tasks/${task.name}")
            val fileName = "${task.name}Result.txt"
            val outputFile = outputDir.map {
                it.file(fileName)
            }

            //TODO RETHINK THIS STATE FAILURE?
            val stateFailure = state.failure

            val outputText: String =
                if (stateFailure != null) onFailureFileText
                else onSuccessFileText

            task.inputs.property("stateFailure", stateFailure)
            task.outputs.file(outputFile)

            task.doLast {
                outputFile.get().asFile.writeText("") // clear file contents
                outputFile.get().asFile.appendText(outputText)
            }
        }
    }
}

