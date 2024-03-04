plugins {
    // id("my-kotlin-plugin")
    alias(libs.plugins.kotlinJvm)
    application
    // id("application-plugin")
    // id("junit-test-plugin")
    // id("kotlin-app-plugin")
}

// tasks.named("checkKotlinGradlePluginConfigurationErrors").configure {
//
//     val output: String =
//         if (state.failure != null) {
//             "Configuration error detected: ${state.failure?.message}"
//         } else {
//             "Check Kotlin Gradle Plugin Configuration Errors task was successful."
//         }
//
//     // assumed up to date unless kotlin build folders change, signifying plugin needs to be re-checked
//     inputs.dir(layout.buildDirectory.dir("classes"))
//     inputs.dir(layout.buildDirectory.dir("kotlin"))
//
//     val outputDir = layout.buildDirectory.dir("extendedCheckKotlinGradlePluginConfigurationErrors")
//     val outputFile = outputDir.map {
//         it.file("extendedCheckKotlinGradlePluginConfigurationErrorsResult.txt")
//     }
//     outputs.file(outputFile)
//
//     doLast {
//         outputFile.get().asFile.writeText("") // clear file contents
//         outputFile.get().asFile.appendText(output)
//     }
// }

tasks.compileJava {
    enabled = false
}

tasks.compileTestJava {
    enabled = false
}

tasks.processResources {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(platform("com.nophasenokill.platform:platform"))

    // applies standard kotlin libs to projects
    implementation("org.jetbrains.kotlin:kotlin-stdlib") {
        isTransitive = false
    }

    implementation(project(":modules:libraries:list")) {
        isTransitive = false
    }
    implementation(project(":modules:libraries:utilities")) {
        isTransitive = false
    }

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
    // implementation("javax.activation:activation")
    implementation("jakarta.activation:jakarta.activation-api")


    implementation("org.apache.commons:commons-text")

    testImplementation(enforcedPlatform("org.junit:junit-bom"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")



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