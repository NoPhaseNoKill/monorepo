import com.nophasenokill.AbstractBuildScanInfoCollectingService
import com.nophasenokill.registerBuildScanInfoCollectingService
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.tooling.events.task.TaskFailureResult
import org.gradle.tooling.events.task.TaskOperationResult
import java.io.Serializable
import java.util.concurrent.CopyOnWriteArrayList


registerBuildScanInfoCollectingService(CollectFailedTaskPathsBuildService::class.java, ::shouldBeReportedAsTeamCityBuildProblem) { failedTasksInBuildLogic, failedTasksInMainBuild ->


    println("Gets here??")
    buildScanPublished {
            ((failedTasksInBuildLogic as List<*>) + (failedTasksInMainBuild as List<*>)).forEach {
                println("##teamcity[buildProblem description='${buildScanUri}/console-log?task=$it']")
            }
        }
}

fun shouldBeReportedAsTeamCityBuildProblem(task: Task) = task is Checkstyle || task is Detekt || task is AbstractCompile || task is CodeNarc

abstract class CollectFailedTaskPathsBuildService : AbstractBuildScanInfoCollectingService() {
    private val failedTaskPaths = CopyOnWriteArrayList<String>()
    override val collectedInformation: Serializable = failedTaskPaths

    override fun shouldInclude(taskPath: String): Boolean {
        // https://github.com/gradle/gradle/issues/21351
        return super.shouldInclude(taskPath) || taskPath.contains("detekt")
    }

    override fun action(taskPath: String, taskResult: TaskOperationResult) {
        if (taskResult is TaskFailureResult) {
            failedTaskPaths.add(taskPath)
        }
    }
}


