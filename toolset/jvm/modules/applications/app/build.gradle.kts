plugins {
    id("kotlin-application-plugin")
}

dependencies {
    implementation(project(":modules:libraries:list"))
    implementation(project(":modules:libraries:utilities"))

    implementation("org.apache.commons:commons-text")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
}