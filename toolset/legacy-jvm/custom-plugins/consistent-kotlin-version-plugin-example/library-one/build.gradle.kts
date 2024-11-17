

/*
    Applying the java plugin before or after the kotlin jvm, seems to have a 'measurable' positive impact on perf compared
    to not applying it at all. Roughly ~10ms

    Important note: Putting a comment in the plugin block ALSO seems to have a measurable impact (~10ms)
 */
plugins {
    id("java")
    id("java-library")
    kotlin("jvm")
}

dependencies {
    api("jakarta.activation:jakarta.activation-api:2.1.3")
    implementation("commons-io:commons-io:2.17.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
}

tasks.test {
    useJUnitPlatform()
}
