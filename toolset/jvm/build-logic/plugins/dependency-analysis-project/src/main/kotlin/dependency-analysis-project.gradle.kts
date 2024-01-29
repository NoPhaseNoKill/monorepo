import com.nophasenokill.DependencyFormatCheck

plugins {
    id("org.jetbrains.kotlin.jvm")
}

val checkDependencyFormattingProject = tasks.register<DependencyFormatCheck>("checkDependencyFormattingProject") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP

    buildFilePath.set(project.buildFile.absolutePath)
    shouldNotHaveVersions.set(true)
    sourceSets.all {
        /*
            The subtle nuance here is that we're using a provider, and not eagerly realizing it with a .get().
            Instead of calling .get (which would no longer lazily evaluate it), we iterate over the providers, whichs
            means that during the configuration phase, we can keep build times to a minimum.

            Comment is copied for:
                - dependency-analysis-project
                - dependency-analysis-platform
         */
        declaredDependencies.put(implementationConfigurationName, configurations.named(implementationConfigurationName).map { configuration -> configuration.dependencies.map { d -> d.toDeclaredString() } })
        declaredDependencies.put(runtimeOnlyConfigurationName, configurations.named(runtimeOnlyConfigurationName).map { configuration -> configuration.dependencies.map { d -> d.toDeclaredString() } })
        declaredDependencies.put(compileOnlyConfigurationName, configurations.named(compileOnlyConfigurationName).map { configuration -> configuration.dependencies.map { d -> d.toDeclaredString() } })

        /*
            Currently no 'go forward' way of using the equivalent of findByName.
            This means that we need to wrap it in a provider so that it's lazily evaluated.
            See table for details: https://docs.gradle.org/current/userguide/task_configuration_avoidance.html#sec:old_vs_new_configuration_api_overview
         */
        declaredDependencies.put(apiConfigurationName, provider { configurations.findByName(apiConfigurationName)?.dependencies?.map { d -> d.toDeclaredString() } ?: emptyList() })
        declaredDependencies.put(compileOnlyApiConfigurationName, provider { configurations.findByName(compileOnlyApiConfigurationName)?.dependencies?.map { d -> d.toDeclaredString() } ?: emptyList() })
    }
}

tasks.check {
    dependsOn(checkDependencyFormattingProject)
    // dependsOn(checkDependencyScopes)
}

fun Dependency.toDeclaredString() = when(this) {
    is ProjectDependency -> ":$name"
    else -> "$group:$name${if (version == null) "" else ":$version"}"
}