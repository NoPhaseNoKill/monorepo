import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("com.nophasenokill.meta-plugins.pin-kotlin-dependency-versions-plugin")
    `java-gradle-plugin`
    alias(libs.plugins.kotlinJvm)
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

    val kotlinLibrary by plugins.creating {
        id = "com.nophasenokill.kotlin-library-plugin"
        implementationClass = "com.nophasenokill.KotlinLibraryPlugin"
    }

    val quotesPlugin by plugins.creating {
        id = "com.nophasenokill.wrap-text-with-quotes-plugin"
        implementationClass = "com.nophasenokill.WrapTextWithQuotesPlugin"
    }
}

dependencies {
    /*
        Kotlin-bom is not required, as the versions are taken of by a mixture of:
            1. id("com.nophasenokill.meta-plugins.pin-kotlin-dependency-versions-plugin")
            2. alias(libs.plugins.kotlinJvm)
     */
    implementation(platform("com.nophasenokill.platforms:generalised-platform"))
    implementation(platform("org.junit:junit-bom"))
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom"))

    implementation("com.nophasenokill.meta-plugins:meta-plugin-one")

    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("commons-io:commons-io")

    testImplementation(gradleTestKit())
}

testing {
    suites {

        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.10.1")

            this.targets.configureEach {
                this.testTask.configure {

                    this.testLogging {

                        // Log events we care about
                        events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
                        displayGranularity = 1

                        // Log everything
                        info {
                            events = setOf(TestLogEvent.STANDARD_ERROR, TestLogEvent.STARTED,TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.PASSED)
                            exceptionFormat = TestExceptionFormat.FULL
                            displayGranularity = 2
                        }

                        // Log everything
                        debug {
                            events = setOf(TestLogEvent.STANDARD_ERROR, TestLogEvent.STARTED,TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.PASSED)
                            exceptionFormat = TestExceptionFormat.FULL
                            displayGranularity = 2
                        }
                    }
                }
            }
            dependencies {
                /*
                   Kotlin-bom is not required, as the versions are taken of by individual test
                   runs and whatever they choose to set it to.
                */
                implementation(platform("com.nophasenokill.platforms:generalised-platform"))
                implementation(platform("org.junit:junit-bom"))
                implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom")) {
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

        val functionalTest by registering(JvmTestSuite::class) {

            /*
                Without this, the top-level test report aggregation will not work, possibly a bug.
             */
            this.testType = TestSuiteType.FUNCTIONAL_TEST

            this.targets.configureEach {
                this.testTask.configure {

                    this.testLogging {

                        // Log events we care about
                        events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
                        displayGranularity = 1

                        // Log everything
                        info {
                            events = setOf(TestLogEvent.STANDARD_ERROR, TestLogEvent.STARTED,TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.PASSED)
                            exceptionFormat = TestExceptionFormat.FULL
                            displayGranularity = 2
                        }

                        // Log everything
                        debug {
                            events = setOf(TestLogEvent.STANDARD_ERROR, TestLogEvent.STARTED,TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.PASSED)
                            exceptionFormat = TestExceptionFormat.FULL
                            displayGranularity = 2
                        }
                    }
                }
            }

            useJUnitJupiter("5.10.1")

            dependencies {
                implementation(project()) // functionalTest test suite depends on the production code in tests
                implementation(platform("com.nophasenokill.platforms:generalised-platform"))
                implementation(platform("org.junit:junit-bom"))
                implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
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
