plugins {
    id("kotlin-library-conventions")
}

dependencies {
    api(project(":modules:libraries:list"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
}
