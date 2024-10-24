import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import java.net.URI
import java.nio.file.Paths


/*
    Configures the maven local repo to a relative path.

    This allows our structure to maintain more flexibility.
 */

abstract class RootProjectPathBuildService : BuildService<RootProjectPathBuildService.Parameters> {
    interface Parameters : BuildServiceParameters {
        val rootProjectPath: Property<String>
    }
}

class MavenLocalRepoPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.pluginManager.withPlugin("java") {

            val serviceProvider: Provider<RootProjectPathBuildService> = project.gradle.sharedServices.registerIfAbsent(
                "rootProjectPathService",
                RootProjectPathBuildService::class.java
            ) { action ->

                action.parameters.rootProjectPath.set(project.rootProject.projectDir.absolutePath)
            }

            val rootProjectPath = serviceProvider.map { it.parameters.rootProjectPath.get() }
            val rootPath = Paths.get(rootProjectPath.get())
            val projectPath = Paths.get(project.projectDir.absolutePath)
            val environmentVariable = project.providers.environmentVariable("PUBLISHING_REPOSITORY_URL")

            // Compute the relative path from the project to the root directory
            val relativePath = projectPath.relativize(rootPath).toString()
            val repo = environmentVariable.getOrElse("$relativePath/local-repo")

            project.repositories.apply {
                maven { mavenArtifactRepo ->
                    mavenArtifactRepo.url = URI(repo)
                }
            }
        }
    }
}
