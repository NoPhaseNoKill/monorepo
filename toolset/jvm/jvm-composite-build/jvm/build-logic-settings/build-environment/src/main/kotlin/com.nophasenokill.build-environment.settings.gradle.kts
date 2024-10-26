

import com.nophasenokill.basics.BuildEnvironmentExtension
import com.nophasenokill.basics.BuildEnvironmentService

with(layout.rootDirectory) {
    gradle.lifecycle.beforeProject {
        val service = gradle.sharedServices.registerIfAbsent("buildEnvironmentService", BuildEnvironmentService::class) {
            check(project.path == ":") {
                // We rely on the fact that root is configured first
                "BuildEnvironmentService should be registered by the root"
            }
            parameters.rootProjectDir = this@with
            parameters.rootProjectBuildDir = project.layout.buildDirectory
        }
        val buildEnvironmentExtension = extensions.create("buildEnvironment", BuildEnvironmentExtension::class)
        buildEnvironmentExtension.gitCommitId = service.flatMap { it.gitCommitId }
        buildEnvironmentExtension.gitBranch = service.flatMap { it.gitBranch }
        buildEnvironmentExtension.repoRoot = this@with
        buildEnvironmentExtension.rootProjectBuildDir = service.flatMap { it.parameters.rootProjectBuildDir }
    }
}
