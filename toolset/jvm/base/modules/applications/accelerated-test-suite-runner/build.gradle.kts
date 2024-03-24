plugins {
    id("com.nophasenokill.kotlin-base-plugin")
    id("application")
}

application {
    mainClass.set("com.nophasenokill.app.AppKt")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    // implementation(platform("com.nophasenokill.platforms:generalised-platform"))
    // testImplementation(platform("com.nophasenokill.platforms:junit-platform"))
    // implementation(platform(project(":platform:generalised-platform")))

    implementation("org.gradle:gradle-tooling-api")
    runtimeOnly("org.slf4j:slf4j-simple")


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

    // testImplementation(platform(project(":platforms:junit-platform")))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}