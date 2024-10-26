
package com.nophasenokill.buildutils.tasks

import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault


@DisableCachingByDefault(because = "Not worth caching")
abstract class CheckSubprojectsInfo : SubprojectsInfo() {

    @TaskAction
    fun checkSubprojectsInfo() {
        if (subprojectsJson.asFile.readText() != generateSubprojectsJson()) {
            throw GradleException(
                "New project(s) added without updating subproject JSON. Please run `:${GenerateSubprojectsInfo.TASK_NAME}` task."
            )
        }
    }
}
