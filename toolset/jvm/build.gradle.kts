import org.jetbrains.kotlin.gradle.plugin.BuildFinishedListenerService


println("Initializing build.gradle.kts for: $name")

plugins {
    `kotlin-dsl` apply false

    /*
        Configures the dependency analysis plugin for all subprojects (libraries/applications),
        which allows us to run './gradle check' that invokes the checkDependencyFormattingProject
        task.

        See: 'dependency-analysis-project' plugin for details
     */
    id("com.autonomousapps.dependency-analysis") version "1.28.0"
}

gradle.projectsEvaluated {
    println("All projects are evaluated (Configuration Phase complete) for ${rootProject.name}")
}

/*
    This is the equivalent of: gradle.buildFinished. However, it is deprecated,
    and the gradle documentation offers no 'inline' alternative. This may
    not be the correct thing to use, but it appears to work exactly how we need.
*/
BuildFinishedListenerService.getInstance(gradle.rootProject).onClose {
    println("Build process finished (Execution Phase complete) for ${rootProject.name}")
}

group = "com.nophasenokill.jvm"