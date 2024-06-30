import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlinJvm)
}


group = "com.nophasenokill.meta-plugins"
version = "0.1.local-dev"

repositories {
    gradlePluginPortal()
}

gradlePlugin {
    val checkKotlinBuildServiceFixPlugin by plugins.creating {
        id = "com.nophasenokill.meta-plugins.check-kotlin-build-service-fix-plugin"
        implementationClass = "com.nophasenokill.CheckKotlinBuildServiceFixPlugin"
    }

    val kotlinMetaPlugin by plugins.creating {
        id = "com.nophasenokill.meta-plugins.pin-kotlin-dependency-versions-plugin"
        implementationClass = "com.nophasenokill.PinKotlinDependencyVersionsPlugin"
    }
}

/*
     Fixes undeclared build service usage when using: enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
     Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165

     Note: Because of the structure of the whole root jvm project, this ALSO needs to be applied directly
     to this level too, as well as exposing a re-usable plugin to fix the SAME issue for other included builds
     or projects.
 */

project.afterEvaluate {
    // Iterate over specific tasks or use a pattern to select tasks
    val tasksOfInterest = project.tasks.withType(DefaultTask::class.java)

    tasksOfInterest.configureEach {
        // Directly interact with the service registrations relevant to Kotlin compilation tasks
        project.gradle.sharedServices.registrations.all {
            val buildServiceProvider = this.service
            val buildService = buildServiceProvider.get()

            val kotlinCollectorSearchString = "org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector"
            if (buildService.toString().contains(kotlinCollectorSearchString)) {
                project.logger.debug(
                    "Applying checkKotlinGradlePluginConfigurationErrors workaround to task: {} for project: {}",
                    this@configureEach.name,
                    project.name
                )
                this@configureEach.usesService(buildServiceProvider)
            }
        }
    }
}


dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${libs.versions.kotlin.get()}")
}

testing {
    suites {

        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit.get())

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
        }

        val functionalTest by registering(JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit.get())

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
                dependencies {
                    implementation(project()) // functionalTest test suite depends on the production code in tests
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${libs.versions.coroutines.get()}")
                    runtimeOnly("org.junit.platform:junit-platform-launcher")
                }
            }
        }
    }
}

tasks.check {
    dependsOn(testing.suites.named("functionalTest"))
}

gradlePlugin {
    testSourceSets(sourceSets["functionalTest"])
}