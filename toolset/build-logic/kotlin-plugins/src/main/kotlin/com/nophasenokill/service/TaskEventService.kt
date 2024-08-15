package com.nophasenokill.service

import com.nophasenokill.domain.GradleTime
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.tooling.events.*


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

    override fun onFinish(event: FinishEvent) {
        println("[Thread-${Thread.currentThread()}] Event ${event.displayName} took: ${event.result.endTime - event.result.startTime}")
    }
}
