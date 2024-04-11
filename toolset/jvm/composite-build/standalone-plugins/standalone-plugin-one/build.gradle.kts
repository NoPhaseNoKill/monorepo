import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
}


group = "com.nophasenokill.standalone-plugins"
version = "0.1.local-dev"

repositories {
    gradlePluginPortal()
}

gradlePlugin {
    val javaBase by plugins.creating {
        id = "com.nophasenokill.java-base-plugin"
        implementationClass = "com.nophasenokill.JavaBasePlugin"
    }

    val kotlinBase by plugins.creating {
        id = "com.nophasenokill.kotlin-base-plugin"
        implementationClass = "com.nophasenokill.KotlinBasePlugin"
    }

    val kotlinApplication by plugins.creating {
        id = "com.nophasenokill.kotlin-application-plugin"
        implementationClass = "com.nophasenokill.KotlinApplicationPlugin"
    }

    val quotesPlugin by plugins.creating {
        id = "com.nophasenokill.wrap-text-with-quotes-plugin"
        implementationClass = "com.nophasenokill.WrapTextWithQuotesPlugin"
    }
}

dependencies {

    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.21"))
    implementation(platform("org.junit:junit-bom:5.10.1"))
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.8.0"))

    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.21") {
        exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core-jvm").because("It conflicts with coroutine BOM which expects 1.8.0 and this brings in 1.5.0")
    }

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("commons-io:commons-io:2.16.0")

    testImplementation(gradleTestKit())
}

testing {
    suites {

        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.10.1")

            this.targets.configureEach {
                this.testTask.configure {
                    this.testLogging {
                        events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
                        showStandardStreams = true
                        minGranularity = 2
                    }
                }
            }
        }

        val functionalTest by registering(JvmTestSuite::class) {

            this.targets.configureEach {
                this.testTask.configure {
                    this.testLogging {
                        events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
                        showStandardStreams = true
                        minGranularity = 2
                    }
                }
            }

            useJUnitJupiter("5.10.1")

            dependencies {
                // functionalTest test suite depends on the production code in tests
                implementation(project())
                implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.21"))
                implementation(platform("org.junit:junit-bom:5.10.1"))
                implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.8.0")) {
                    exclude("org.jetbrains", "annotations")
                }

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test") {
                    exclude("org.jetbrains", "annotations")
                }
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm") {
                    exclude("org.jetbrains", "annotations")
                }

                implementation("commons-io:commons-io:2.16.0")
            }
        }
    }
}

gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])


val functionalTestTask = tasks.register("functionalTestTask") {


    inputs.files("src/functionalTest")
    inputs.files(tasks.jar)
    outputs.dirs(layout.buildDirectory.get().asFile.resolve("test-results/functionalTest"))
    outputs.dirs(layout.buildDirectory.get().asFile.resolve("classes/functionalTest"))
    outputs.dirs(layout.buildDirectory.get().asFile.resolve("kotlin/compileFunctionalTestKotlin"))

    dependsOn(tasks.jar)
    dependsOn(testing.suites.named("functionalTest"))
}

tasks.test {
    inputs.files("src/test")
    inputs.files(tasks.jar)
    outputs.dirs(layout.buildDirectory.get().asFile.resolve("test-results/test"))
    outputs.dirs(layout.buildDirectory.get().asFile.resolve("classes/test"))
    outputs.dirs(layout.buildDirectory.get().asFile.resolve("kotlin/compileTestKotlin"))

    dependsOn(tasks.jar)
}

tasks.named<Task>("check") {
    // Include functionalTest as part of the check which implicitly means build lifecycle
    dependsOn(functionalTestTask)
}
