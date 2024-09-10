import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.kotlinDsl)
}

gradlePlugin {
    plugins {
        create("javaBasePlugin") {
            id = "com.nophasenokill.java-base-plugin"
            implementationClass = "com.nophasenokill.JavaBasePlugin"
        }

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

        create("jacocoPlugin") {
            id = "com.nophasenokill.jacoco-plugin"
            implementationClass = "com.nophasenokill.JacocoPlugin"
        }

        create("hashingTasksPlugin") {
            id = "com.nophasenokill.hashing-tasks-plugin"
            implementationClass = "com.nophasenokill.HashingTasksPlugin"
        }

        create("packageNameRelocatorPlugin") {
            id = "com.nophasenokill.package-name-relocator-plugin"
            implementationClass = "com.nophasenokill.PackageNameRelocatorPlugin"
        }

        create("taskEventsPlugin") {
            id = "com.nophasenokill.task-events-plugin"
            implementationClass = "com.nophasenokill.TaskEventsPlugin"
        }

        create("ideaSourcesDownloadPlugin") {
            id = "com.nophasenokill.idea-sources-download-plugin"
            implementationClass = "com.nophasenokill.IdeaSourcesDownloadPlugin"
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
    implementation(projects.metaGradleUtilities)
    implementation("org.jetbrains.compose:compose-gradle-plugin:${libs.versions.composePlugin.get()}")

    // only exists from v2 onwards
    if(libs.versions.kotlin.get() >= "2.0.0") {
        implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:${libs.versions.kotlin.get()}")
    } else {
        println("""
            Not including org.jetbrains.kotlin:compose-compiler-gradle-plugin dependency. Found kotlin version needs to be
            2.0.0 or above for this dependency to make sense
        """.trimIndent())
    }


    implementation("org.ow2.asm:asm:9.2")
    implementation("org.ow2.asm:asm-commons:9.2")
    implementation("org.ow2.asm:asm-util:9.2")
    implementation("org.benf:cfr:0.152")

    implementation("me.lucko:jar-relocator:1.7")

    implementation("app.cash.sqldelight:gradle-plugin:${libs.versions.sqldelight.get()}")


    testImplementation("org.junit.jupiter:junit-jupiter-api:${libs.versions.junit.get()}")

    /*
        These are required, so we don't implicitly load test framework. https://docs.gradle.org/8.7/userguide/upgrading_version_8.html#test_framework_implementation_dependencies
     */
    testImplementation("org.junit.jupiter:junit-jupiter:${libs.versions.junit.get()}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${libs.versions.junitPlatform.get()}")
}

// // Share the test report data to be aggregated for the whole project
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

