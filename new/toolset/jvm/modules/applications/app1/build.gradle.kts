plugins {
    id("my-plugin")
    id("application")

}

dependencies {
    implementation(project(":modules:libraries:lib1"))
}