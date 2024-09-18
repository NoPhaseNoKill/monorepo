package com.nophasenokill.service

import com.nophasenokill.domain.GradleTime
import org.gradle.api.Project
import org.gradle.api.logging.Logging
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.tooling.events.FinishEvent
import org.gradle.tooling.events.OperationCompletionListener
import org.gradle.tooling.events.OperationResult


abstract class TaskEventsService : BuildService<BuildServiceParameters.None>,
    OperationCompletionListener,
    OperationResult
    /*
        Possibly add these in future?

        ProgressListener,
        ProgressEvent
     */
{
    companion object {
        val LOGGER = Logging.getLogger(TaskEventsService::class.java)
    }
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
            LOGGER.lifecycle("Plugin applied: ${this.javaClass.name}")
            println("Plugin applied: ${this.javaClass.name}")
        }
    }

    override fun onFinish(event: FinishEvent) {
        LOGGER.lifecycle("Finish event is: ${event}")
        LOGGER.lifecycle("[Thread-${Thread.currentThread()}] Event ${event.displayName} took: ${event.result.endTime - event.result.startTime}")
    }
}
