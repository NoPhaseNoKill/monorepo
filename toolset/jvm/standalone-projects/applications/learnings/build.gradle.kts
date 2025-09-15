
plugins {
    id("com.nophasenokill.exposed.java-version-checker-plugin")
    id("com.nophasenokill.exposed.group-and-version-details-plugin")
    id("com.nophasenokill.exposed.repositories-plugin")
    id("com.nophasenokill.exposed.kotlin-application-plugin")
}

dependencies {
    testImplementation(projects.testingLibraryOne)
}
