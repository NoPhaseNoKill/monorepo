plugins {
    id("com.nophasenokill.kotlin-base-plugin")
    `java-library`
}

tasks.withType(JavaCompile::class.java).configureEach {
    enabled = false
}

tasks.processResources {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("com.nophasenokill.standalone-plugins:plugin")
    // implementation(platform("com.nophasenokill.platforms:generalised-platform"))
    // testImplementation(platform("com.nophasenokill.platforms:junit-platform"))

    api(projects.libraries.list)


    // testImplementation(platform(project(":platforms:junit-platform")))
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
