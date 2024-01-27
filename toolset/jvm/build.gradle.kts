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


group = "com.nophasenokill.jvm"