plugins {
    id("kotlin-library-plugin")
}

dependencies {
    api(project(":modules:libraries:list"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
}
