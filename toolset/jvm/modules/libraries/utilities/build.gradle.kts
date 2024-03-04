plugins {
    // id("my-kotlin-plugin")
    alias(libs.plugins.kotlinJvm)
    `java-library`

    // kotlin("jvm")
    // id("library-plugin")
    // id("junit-test-plugin")
    // id("kotlin-lib-plugin")
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

    api(project(":modules:libraries:list")) {
        isTransitive = false
    }

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
    // implementation("org.apache.commons:commons-text:1.10.0")
    // testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
}
