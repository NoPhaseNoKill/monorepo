plugins {
    kotlin("jvm") version "1.9.20"
    id("application")
    id("my-plugin")
}

dependencies {
    implementation(project(":modules:libraries:lib1"))
}