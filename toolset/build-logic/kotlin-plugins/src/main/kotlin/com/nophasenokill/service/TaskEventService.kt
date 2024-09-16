package com.nophasenokill.service

import com.nophasenokill.domain.GradleTime
import org.gradle.api.Project
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.tooling.events.*
import org.gradle.tooling.events.configuration.ProjectConfigurationOperationResult


abstract class TaskEventsService : BuildService<BuildServiceParameters.None>,
    OperationCompletionListener,
    OperationResult
    /*
        Possibly add these in future?

        ProgressListener,
        ProgressEvent
     */
{
    override fun getStartTime(): Long {
        val time = GradleTime.now()
        return time.current
    }

    override fun getEndTime(): Long {
        val time = GradleTime.now()
        return time.current
    }

    // Function to register a plugin listener on the current project
    fun registerPluginListener(project: Project) {
        project.plugins.all {
            println("Plugin applied: ${this.javaClass.name}")
        }
    }

    override fun onFinish(event: FinishEvent) {
        println("Finish event is: ${event}")
        println("[Thread-${Thread.currentThread()}] Event ${event.displayName} took: ${event.result.endTime - event.result.startTime}")
    }
}
