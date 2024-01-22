import com.nophasenokill.DependencyFormatCheck

plugins {
    id("java-platform")
}

val checkDependencyFormattingPlatform = tasks.register<DependencyFormatCheck>("checkDependencyFormattingPlatform") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP

    buildFilePath.set(project.buildFile.absolutePath)
    shouldNotHaveVersions.set(false)

    /*
        The subtle nuance here is that we're using a provider, and not eagerly realizing it with a .get().
        Instead of calling .get (which would no longer lazily evaluate it), we iterate over the providers, whichs
        means that during the configuration phase, we can keep build times to a minimum.

        Comment is copied for:
            - dependency-analysis-project
            - dependency-analysis-platform
     */
    declaredDependencies.put("api",  configurations.api.map { configuration -> configuration.dependencies.map { d -> d.toDeclaredString() }})
    declaredDependencies.put("runtime",  configurations.runtime.map { configuration -> configuration.dependencies.map { d -> d.toDeclaredString() }})

    // these are not duplicate lines like intellij says
    declaredDependencies.put("api",  configurations.api.map { configuration -> configuration.dependencyConstraints.map { d -> d.toDeclaredString() }})
    declaredDependencies.put("runtime",  configurations.runtime.map { configuration -> configuration.dependencyConstraints.map { d -> d.toDeclaredString() }})
}

tasks.check {
    dependsOn(checkDependencyFormattingPlatform)
}

fun Dependency.toDeclaredString() = "$group:$name:$version"
fun DependencyConstraint.toDeclaredString() = "$group:$name:$version"