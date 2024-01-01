plugins {
    id("integraboost-java-base-plugin")
    id("application")

}

dependencies {
    implementation(project(":modules:libraries:lib1"))
}