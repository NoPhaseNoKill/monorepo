plugins {
    id("kotlin-library-plugin")
}

dependencies {
    implementation(platform("com.nophasenokill.platform:platform"))
    //
    // testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    //
    // testImplementation("org.junit.jupiter:junit-jupiter-api")

    // implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    // testImplementation(enforcedPlatform("com.nophasenokill.platform:platform"))

    // testImplementation("org.junit.jupiter:junit-jupiter-api")
    // testImplementation("org.junit.jupiter:junit-jupiter")
    // testRuntimeOnly("org.junit.platform:junit-platform-launcher")

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
