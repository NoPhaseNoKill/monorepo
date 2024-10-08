import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.kotlinDsl)
}

group = "com.nophasenokill.$name"
version = "0.1.local-dev"

gradlePlugin {
    plugins {
        create("kotlinBasePlugin") {
            id = "com.nophasenokill.kotlin-base-plugin"
            implementationClass = "com.nophasenokill.KotlinBasePlugin"
        }

        create("kotlinLibraryPlugin") {
            id = "com.nophasenokill.kotlin-library-plugin"
            implementationClass = "com.nophasenokill.KotlinLibraryPlugin"
        }

        create("kotlinApplicationPlugin") {
            id = "com.nophasenokill.kotlin-application-plugin"
            implementationClass = "com.nophasenokill.KotlinApplicationPlugin"
        }

        create("kotlinDesktopApplicationPlugin") {
            id = "com.nophasenokill.kotlin-desktop-application-plugin"
            implementationClass = "com.nophasenokill.KotlinDesktopApplicationPlugin"
        }

        create("hashingTasksPlugin") {
            id = "com.nophasenokill.hashing-tasks-plugin"
            implementationClass = "com.nophasenokill.HashingTasksPlugin"
        }

        create("taskEventsPlugin") {
            id = "com.nophasenokill.task-events-plugin"
            implementationClass = "com.nophasenokill.TaskEventsPlugin"
        }

        create("ideaSourcesDownloadPlugin") {
            id = "com.nophasenokill.idea-sources-download-plugin"
            implementationClass = "com.nophasenokill.IdeaSourcesDownloadPlugin"
        }

        create("testReportDataConsumerPlugin") {
            id = "com.nophasenokill.test-report-data-consumer-plugin"
            implementationClass = "com.nophasenokill.TestReportDataConsumerPlugin"
        }

        create("testReportDataProviderPlugin") {
            id = "com.nophasenokill.test-report-data-producer-plugin"
            implementationClass = "com.nophasenokill.TestReportDataProviderPlugin"
        }

        create("incrementalTestPlugin") {
            id = "com.nophasenokill.incremental-test-plugin"
            implementationClass = "com.nophasenokill.IncrementalTestPlugin"
        }
        create("minifyExternalDependenciesPlugin") {
            id = "com.nophasenokill.minify-dependencies-plugin"
            implementationClass = "com.nophasenokill.MinifyExternalDependenciesPlugin"
        }

        create("localPublishingPlugin") {
            id = "com.nophasenokill.local-publishing-plugin"
            implementationClass = "com.nophasenokill.LocalPublishingPlugin"
        }


    }
}

dependencies {
    /*
        Does not need this due to: applyDependencies() method inside of JavaGradlePluginPlugin (`java-gradle-plugin`)
        already applying this.

        implementation(gradleApi())
     */

    /*
        Equivalent of: implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${libs.versions.kotlin.get()}")
     */
    implementation(kotlin("gradle-plugin", libs.versions.kotlin.get()))
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${libs.versions.coroutines.get()}")

    implementation("com.nophasenokill.meta-byte-buddy:meta-byte-buddy:0.1.local-dev")
    implementation("com.nophasenokill.meta-gradle-utilities:meta-gradle-utilities:0.1.local-dev")
    implementation("org.jetbrains.compose:compose-gradle-plugin:${libs.versions.composePlugin.get()}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")

    implementation("org.ow2.asm:asm:9.7")
    implementation("org.ow2.asm:asm-commons:9.7")
    implementation("org.ow2.asm:asm-util:9.7")
    implementation("org.benf:cfr:0.152")

    // only exists from v2 onwards
    if(libs.versions.kotlin.get() >= "2.0.0") {
        implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:${libs.versions.kotlin.get()}")
    } else {
        println("""
            Not including org.jetbrains.kotlin:compose-compiler-gradle-plugin dependency. Found kotlin version needs to be
            2.0.0 or above for this dependency to make sense
        """.trimIndent())
    }
    implementation("app.cash.sqldelight:gradle-plugin:${libs.versions.sqldelight.get()}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${libs.versions.junit.get()}")

    /*
        These are required, so we don't implicitly load test framework. https://docs.gradle.org/8.7/userguide/upgrading_version_8.html#test_framework_implementation_dependencies
     */
    testImplementation("org.junit.jupiter:junit-jupiter:${libs.versions.junit.get()}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${libs.versions.junitPlatform.get()}")
}

// Share the test report data to be aggregated for the whole project
configurations.create("binaryTestResultsElements") {
    isCanBeResolved = false
    isCanBeConsumed = true
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("test-report-data"))
    }
    outgoing.artifact(tasks.test.map { task -> task.getBinaryResultsDirectory().get() })
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

    testLogging.minGranularity = 2
}

