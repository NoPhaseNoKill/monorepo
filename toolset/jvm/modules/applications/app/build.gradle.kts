plugins {
    id("kotlin-application-plugin")
}

dependencies {
    //
    // api(enforcedPlatform("com.nophasenokill.platform:platform"))
    // testImplementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    //
    // // testImplementation("org.junit.jupiter:junit-jupiter")
    // // testImplementation("org.junit.jupiter:junit-jupiter-api")
    // //
    // // testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // // // implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    // //

    implementation(project(":libraries:list"))
    implementation(project(":libraries:utilities"))

    /*
        This is used to test/confirm that the capability conflict plugin is working correctly.

        See capability conflict plugin for more details.

            With  capability conflict plugin enabled:
            > Task app:run
            file:/home/REDACTED/.gradle/caches/modules-2/files-2.1/jakarta.activation/jakarta.activation-api/1.2.2/99f53adba383cb1bf7c3862844488574b559621f/jakarta.activation-api-1.2.2.jar


            Without this:
            > Task :app:run
            file:/home/REDACTED/.gradle/caches/modules-2/files-2.1/javax.activation/activation/1.1.1/485de3a253e23f645037828c07f1d7f1af40763a/activation-1.1.1.jar
     */
    // implementation("javax.activation:activation:1.1.1")
    implementation("jakarta.activation:jakarta.activation-api")
    implementation("javax.activation:activation")


    implementation("org.apache.commons:commons-text")

    // testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // testImplementation("org.junit.jupiter:junit-jupiter-api")


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