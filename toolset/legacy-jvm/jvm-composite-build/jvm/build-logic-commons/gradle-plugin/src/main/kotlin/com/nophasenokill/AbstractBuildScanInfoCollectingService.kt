package com.nophasenokill

import org.gradle.api.provider.SetProperty
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.tooling.events.FinishEvent
import org.gradle.tooling.events.OperationCompletionListener
import org.gradle.tooling.events.task.TaskFinishEvent
import org.gradle.tooling.events.task.TaskOperationResult
import java.io.Serializable

/**
 * A BuildService which monitors a few tasks (in both build-logic and main build) and collects information for build scan.
 * It's currently implemented by two use cases:
 * 1. Collect cache misses for compilation tasks and publish a `CACHE_MISS` tag for build scan.
 * 2. Collect failed task paths and display a link which points to the corresponding task in the build scan, like `https://ge.gradle.org/s/xxx/console-log?task=yyy`.
 */
abstract class AbstractBuildScanInfoCollectingService : BuildService<AbstractBuildScanInfoCollectingService.Params>, OperationCompletionListener {
    /**
     * To be compatible with configuration cache, this field must be Serializable.
     */
    abstract val collectedInformation: Serializable

    interface Params : BuildServiceParameters {
        val monitoredTaskPaths: SetProperty<String>
    }

    override fun onFinish(event: FinishEvent) {
        if (event !is TaskFinishEvent) {
            return
        }

        val taskPath = event.descriptor.taskPath
        if (shouldInclude(taskPath)) {
            action(taskPath, event.result)
        }
    }

    protected open fun shouldInclude(taskPath: String): Boolean = parameters.monitoredTaskPaths.get().contains(taskPath)

    abstract fun action(taskPath: String, taskResult: TaskOperationResult)
}
