println("Initializing build.gradle.kts for: $name")

plugins {
    id("kotlin-application-plugin")
}

dependencies {
    implementation(project(":modules:libraries:list"))
    implementation(project(":modules:libraries:utilities"))

    implementation("org.apache.commons:commons-text")

    /*
        This is excluded from dependency analysis failure (ie allowed/expected behaviour currently)
        TODO confirm whether we cant just declare it on the plugin level rather than for each project
     */
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")

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
//     implementation("org.apache.commons:commons-text:1.10.0")
//     testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
}