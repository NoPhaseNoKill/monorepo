plugins {
    id("kotlin-application-conventions")
}

dependencies {
    implementation("org.apache.commons:commons-text")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
    implementation(project(":modules:libraries:utilities"))
    implementation(project(":modules:libraries:list"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")

}

application {
    // Define the main class for the application.
    mainClass.set("app.AppKt")
}
