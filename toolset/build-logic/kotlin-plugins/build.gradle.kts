plugins {
    alias(libs.plugins.kotlinJvm)
    `java-gradle-plugin`
    jacoco
}

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

        create("jacocoPlugin") {
            id = "com.nophasenokill.jacoco-plugin"
            implementationClass = "com.nophasenokill.JacocoPlugin"
        }

        create("hashingTasksPlugin") {
            id = "com.nophasenokill.hashing-tasks-plugin"
            implementationClass = "com.nophasenokill.HashingTasksPlugin"
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

    implementation("org.ow2.asm:asm:9.2")
    implementation("org.ow2.asm:asm-commons:9.2")
    implementation("org.ow2.asm:asm-util:9.2")
    implementation("org.benf:cfr:0.152")


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
