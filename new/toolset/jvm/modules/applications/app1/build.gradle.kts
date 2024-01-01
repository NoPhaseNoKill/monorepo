plugins {
    id("integraboost.plugin.module.java-project-plugin")
    id("application")
}

dependencies {
    implementation(project(":modules:libraries:lib1"))
}