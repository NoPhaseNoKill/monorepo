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


    testImplementation("org.junit.jupiter:junit-jupiter-api:${libs.versions.junit.get()}")

    /*
        These are required, so we don't implicitly load test framework. https://docs.gradle.org/8.7/userguide/upgrading_version_8.html#test_framework_implementation_dependencies
     */
    testImplementation("org.junit.jupiter:junit-jupiter:${libs.versions.junit.get()}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${libs.versions.junitPlatform.get()}")
}
//
//
//
// tasks.jacocoTestReport {
//     reports {
//         xml.required = false
//         csv.required = false
//         html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
//     }
// }
//
//
//
//
