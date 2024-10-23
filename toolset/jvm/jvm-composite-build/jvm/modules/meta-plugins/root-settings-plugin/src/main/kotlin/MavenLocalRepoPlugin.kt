import java.net.URI
import java.nio.file.Paths
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property

/*
    Registers and configures a task which allows us to point the maven local to a relative path
    to root from the project itself
 */
class MavenLocalRepoPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        val serviceProvider: Provider<RootProjectPathService> = project.gradle.sharedServices.registerIfAbsent(
            "rootProjectPathService",
            RootProjectPathService::class.java
        ) { action ->

            action.parameters.rootProjectPath.set(project.rootProject.projectDir.absolutePath)
        }

        project.tasks.register("configureMavenLocalRepoAndPrintRootPath", ConfigureMavenLocalRepoAndPrintTask::class.java) { task ->
            task.rootProjectPath.set(serviceProvider.map { it.parameters.rootProjectPath.get() })
            task.environmentVariable.set(project.providers.environmentVariable("PUBLISHING_REPOSITORY_URL"))
            task.outputFile.set(project.layout.buildDirectory.file("shared-build-services/root-project-path/output.txt"))
        }

        project.tasks.all { task ->
            if(task.name !== "configureMavenLocalRepoAndPrintRootPath") {
                task.dependsOn("configureMavenLocalRepoAndPrintRootPath")
            }
        }
    }
}


abstract class RootProjectPathService : BuildService<RootProjectPathService.Parameters> {
    interface Parameters : BuildServiceParameters {
        val rootProjectPath: Property<String>
    }
}


abstract class ConfigureMavenLocalRepoAndPrintTask : DefaultTask() {

    @get:Input
    abstract val rootProjectPath: Property<String>

    @get:Input
    @get:Optional
    abstract val environmentVariable: Property<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun configureMavenRepoAndPrintPath() {
        val output = outputFile.get().asFile
        output.writeText(rootProjectPath.get())

        val rootPath = Paths.get(rootProjectPath.get())
        val projectPath = Paths.get(project.projectDir.absolutePath)

        // Compute the relative path from the project to the root directory
        val relativePath = projectPath.relativize(rootPath).toString()

        // Configure the Maven local repository using the computed relative path or the environment variable
        project.repositories.apply {
            maven { mavenArtifactRepo ->
                mavenArtifactRepo.url = URI(environmentVariable.getOrElse("$relativePath/local-repo"))
            }
        }

        println("""
            Root project path: ${rootProjectPath.get()}
            Maven local repository configured for project: ${project.name}.
            The relative path from this project to the configured Maven plugins repo is: $relativePath/local-repo
        """.trimIndent())
    }
}

