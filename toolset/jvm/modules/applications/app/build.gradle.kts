plugins {
    id("kotlin-application-conventions")
}

dependencies {
    implementation("org.apache.commons:commons-text")
    implementation(project(":modules:libraries:utilities"))
}

application {
    // Define the main class for the application.
    mainClass.set("app.AppKt")
}
