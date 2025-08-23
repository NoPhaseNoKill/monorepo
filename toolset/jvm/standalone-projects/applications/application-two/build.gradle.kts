plugins {
    id("com.nophasenokill.exposed.java-version-checker-plugin")
    id("com.nophasenokill.exposed.plugin-base")
    id("com.nophasenokill.exposed.group-and-version-details-plugin")
    id("com.nophasenokill.exposed.repositories-plugin")
    id("com.nophasenokill.exposed.kotlin-application-plugin")
    id("com.nophasenokill.exposed.incremental-test-plugin")
}

dependencies {
    /*
        Equivalent of: testImplementation(project(":testing-library-one"))
    */
    testImplementation(projects.testingLibraryOne)
}
