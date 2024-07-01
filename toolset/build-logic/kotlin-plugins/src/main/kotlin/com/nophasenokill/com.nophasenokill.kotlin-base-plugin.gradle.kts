import gradle.kotlin.dsl.accessors._d9dcfd1a467b0b6fe90c5571a57aa558.implementation
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("jvm")
}

afterEvaluate {

    group = "com.nophasenokill"
    version = "0.1.local-dev"

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    val versionCatalog = extensions.findByType(VersionCatalogsExtension::class.java)?.named("libs")
    val junitVersion = requireNotNull(versionCatalog?.findVersion("junit")?.get())
    val junitPlatformVersion = requireNotNull(versionCatalog?.findVersion("junitPlatform")?.get())

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib:$embeddedKotlinVersion")

        testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")

        /*
            These are required so we don't implicitly load test framework. https://docs.gradle.org/8.7/userguide/upgrading_version_8.html#test_framework_implementation_dependencies
         */
        testImplementation("org.junit.jupiter:junit-jupiter:${junitVersion}")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher:${junitPlatformVersion}")
    }

    val testTasks = tasks.withType(Test::class.java)

    testTasks.configureEach {
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

