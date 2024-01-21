import com.nophasenokill.DependencyFormatCheck

plugins {
    id("java-platform")
}

val checkDependencyFormattingPlatform = tasks.register<DependencyFormatCheck>("checkDependencyFormattingPlatform") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP

    buildFilePath.set(project.buildFile.absolutePath)
    shouldNotHaveVersions.set(false)
    declaredDependencies.put("api", provider { configurations.api.get().dependencies.map { d -> d.toDeclaredString() } })
    declaredDependencies.put("runtime", provider { configurations.runtime.get().dependencies.map { d -> d.toDeclaredString() } })

    // these are not duplicate lines like intellij says
    declaredConstraints.put("api", provider { configurations.api.get().dependencyConstraints.map { d -> d.toDeclaredString() } })
    declaredConstraints.put("runtime", provider { configurations.runtime.get().dependencyConstraints.map { d -> d.toDeclaredString() } })
}

tasks.check {
    dependsOn(checkDependencyFormattingPlatform)
}

fun Dependency.toDeclaredString() = "$group:$name:$version"
fun DependencyConstraint.toDeclaredString() = "$group:$name:$version"