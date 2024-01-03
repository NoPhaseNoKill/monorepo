plugins {
    id("integraboost.plugin.module.java-project-plugin")
    id("application")
}

application {
    mainClass.set("integraboost.App1")
}

dependencies {
    implementation(project(":modules:libraries:lib1"))
}