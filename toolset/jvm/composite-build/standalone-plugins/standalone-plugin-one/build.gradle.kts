import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("com.nophasenokill.meta-plugins.check-kotlin-build-service-fix-plugin") // Only applies it to project standalone-plugins (aka this one)
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
    implementation("com.nophasenokill.meta-plugins:meta-plugin-one")
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

                    maxParallelForks = Runtime.getRuntime().availableProcessors().div(2)
                    // forkEvery = 1

                    this.testLogging {

                        logger.isEnabled(LogLevel.LIFECYCLE)

                        // Log events we care about, show exception as short
                        events = setOf(TestLogEvent.STANDARD_OUT, TestLogEvent.FAILED)
                        exceptionFormat = TestExceptionFormat.SHORT
                        displayGranularity = -1

                        // Log everything
                        info {
                            events = setOf(TestLogEvent.STANDARD_ERROR, TestLogEvent.STARTED,TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.PASSED)
                            exceptionFormat = TestExceptionFormat.FULL
                            displayGranularity = -1
                        }

                        // Log everything
                        debug {
                            events = setOf(TestLogEvent.STANDARD_ERROR, TestLogEvent.STARTED,TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.PASSED)
                            exceptionFormat = TestExceptionFormat.FULL
                            displayGranularity = -1
                        }
                    }
                }
            }
        }

        val functionalTest by registering(JvmTestSuite::class) {

            this.targets.configureEach {
                this.testTask.configure {

                    maxParallelForks = Runtime.getRuntime().availableProcessors().div(2)
                    // forkEvery = 1

                    this.testLogging {

                        logger.isEnabled(LogLevel.LIFECYCLE)

                        // Log events we care about, show exception as short
                        events = setOf(TestLogEvent.STANDARD_OUT, TestLogEvent.FAILED)
                        exceptionFormat = TestExceptionFormat.SHORT
                        displayGranularity = -1

                        // Log everything
                        info {
                            events = setOf(TestLogEvent.STANDARD_ERROR, TestLogEvent.STARTED,TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.PASSED)
                            exceptionFormat = TestExceptionFormat.FULL
                            displayGranularity = -1
                        }

                        // Log everything
                        debug {
                            events = setOf(TestLogEvent.STANDARD_ERROR, TestLogEvent.STARTED,TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.PASSED)
                            exceptionFormat = TestExceptionFormat.FULL
                            displayGranularity = -1
                        }
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
            }
        }
    }
}

gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])


val functionalTestTask = tasks.register("functionalTestTask") {
    dependsOn(testing.suites.named("functionalTest"))
}

tasks.check {
    // Include functionalTest as part of the check which implicitly means build lifecycle
    dependsOn(functionalTestTask)
}
