plugins {
    id("kotlin-application-plugin")
}

dependencies {
    implementation(project(":modules:libraries:utilities"))
    implementation(project(":modules:libraries:list"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    implementation("org.apache.commons:commons-text:1.10.0")
}