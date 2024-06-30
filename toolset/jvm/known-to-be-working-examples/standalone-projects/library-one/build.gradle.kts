import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("com.nophasenokill.basic-plugin")
    `java-library`

}

dependencies {

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1") // for assertions and libraries during tests

    /*
        To not implicitly load test framework. https://docs.gradle.org/8.7/userguide/upgrading_version_8.html#test_framework_implementation_dependencies
     */
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()

    testLogging.events = setOf(
        TestLogEvent.STANDARD_OUT,
        TestLogEvent.STARTED,
        TestLogEvent.PASSED,
        TestLogEvent.SKIPPED,
        TestLogEvent.FAILED,
        TestLogEvent.STANDARD_OUT,
        TestLogEvent.STANDARD_ERROR,
    )
}