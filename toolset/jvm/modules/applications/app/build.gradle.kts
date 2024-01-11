plugins {
    id("kotlin-application-conventions")
}

dependencies {
    implementation("org.apache.commons:commons-text")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(project(":modules:libraries:utilities"))
    implementation(project(":modules:libraries:list"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")

}

application {
    // Define the main class for the application.
    mainClass.set("app.AppKt")
}
