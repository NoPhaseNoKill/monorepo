plugins {
    id("kotlin-application-conventions")
}

dependencies {
    implementation("org.apache.commons:commons-text")
    implementation(project(":modules:libraries:utilities"))
    implementation(project(":modules:libraries:list"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
}


