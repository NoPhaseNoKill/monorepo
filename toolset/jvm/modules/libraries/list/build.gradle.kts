plugins {
    id("kotlin-library-plugin")

}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api")

    /*
        Both of these should error when uncommented due to to com.nophasenokill.dependency-analysis-project.gradle.kts.
        That plugin is meant to prevent project level dependency level declarations.

        To test it's still working, uncomment and run ./gradlew checkDependencyFormatting

        This comment has been copied into:
            - applications/app
            - libraries/list
            - libraries/utilities
    */
    // implementation("org.apache.commons:commons-text:1.10.0")
    // testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
}
