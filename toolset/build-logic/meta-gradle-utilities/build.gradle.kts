import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)

}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("gradle-plugin", libs.versions.kotlin.get()))

    implementation("app.cash.sqldelight:gradle-plugin:${libs.versions.sqldelight.get()}")

    implementation("org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:${libs.versions.kotlinDsl.get()}")
    implementation(kotlin("gradle-plugin", libs.versions.kotlin.get()))
    testImplementation("org.junit.jupiter:junit-jupiter-api:${libs.versions.junit.get()}")

    /*
        These are required, so we don't implicitly load test framework. https://docs.gradle.org/8.7/userguide/upgrading_version_8.html#test_framework_implementation_dependencies
     */
    testImplementation("org.junit.jupiter:junit-jupiter:${libs.versions.junit.get()}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${libs.versions.junitPlatform.get()}")
}

tasks {
    test {

        // Fixes: https://github.com/gradle/gradle/issues/18647
        jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")

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
}

