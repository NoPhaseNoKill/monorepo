import com.autonomousapps.DependencyAnalysisSubExtension
import com.nophasenokill.DependencyFormatCheck
import com.nophasenokill.DependencyScopeCheck

plugins {
    id("org.jetbrains.kotlin.jvm")
}

val checkDependencyFormatting = tasks.register<DependencyFormatCheck>("checkDependencyFormatting") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP

    buildFilePath.set(project.buildFile.absolutePath)
    shouldNotHaveVersions.set(true)
    sourceSets.all {
        declaredDependencies.put(implementationConfigurationName, provider { configurations.getByName(implementationConfigurationName).dependencies.map { d -> d.toDeclaredString() } })
        declaredDependencies.put(runtimeOnlyConfigurationName, provider { configurations.getByName(runtimeOnlyConfigurationName).dependencies.map { d -> d.toDeclaredString() } })
        declaredDependencies.put(compileOnlyConfigurationName, provider { configurations.getByName(compileOnlyConfigurationName).dependencies.map { d -> d.toDeclaredString() } })
        declaredDependencies.put(apiConfigurationName, provider { configurations.findByName(apiConfigurationName)?.dependencies?.map { d -> d.toDeclaredString() } ?: emptyList() })
        declaredDependencies.put(compileOnlyApiConfigurationName, provider { configurations.findByName(compileOnlyApiConfigurationName)?.dependencies?.map { d -> d.toDeclaredString() } ?: emptyList() })
    }
}

val checkDependencyScopes = tasks.register<DependencyScopeCheck>("checkDependencyScopes") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    shouldRunAfter(checkDependencyFormatting)
}

tasks.check {
    dependsOn(checkDependencyFormatting)
    dependsOn(checkDependencyScopes)
}

plugins.withId("com.autonomousapps.dependency-analysis") {
    extensions.getByType<DependencyAnalysisSubExtension>().registerPostProcessingTask(checkDependencyScopes)
}

fun Dependency.toDeclaredString() = when(this) {
    is ProjectDependency -> ":$name"
    else -> "$group:$name${if (version == null) "" else ":$version"}"
}