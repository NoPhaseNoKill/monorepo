plugins {
    // id("my-kotlin-plugin")
    kotlin("jvm")
    id("library-plugin")
    id("junit-test-plugin")
    // id("kotlin-lib-plugin")
}

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))

    // applies standard kotlin libs to projects
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    api(project(":modules:libraries:list"))

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
