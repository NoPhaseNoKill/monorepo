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

gradle.taskGraph.whenReady {
    val allTasks = gradle.taskGraph.allTasks
    allTasks.forEach {
        gradle.sharedServices.registrations.all {
            val buildServiceProvider = this.service
            val buildService = buildServiceProvider.get()

            val kotlinCollectorSearchString = "org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector"
            val isCollectorService = buildService.toString().contains(kotlinCollectorSearchString)

            if (isCollectorService) {
                project.logger.debug(
                    "Applying checkKotlinGradlePluginConfigurationErrors workaround to task: {} for project: {}",
                    it,
                    project.name
                )
                it.usesService(buildServiceProvider)
            }
        }
    }
}

dependencies {
    implementation(platform("com.nophasenokill.platforms:generalised-platform"))
    implementation(platform("org.junit:junit-bom"))
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom"))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
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
                dependencies {
                    implementation(platform("com.nophasenokill.platforms:generalised-platform"))
                    implementation(platform("org.junit:junit-bom"))
                    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom"))

                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
                }
            }
        }

        val functionalTest by registering(JvmTestSuite::class) {
            useJUnitJupiter("5.10.1")

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
}

gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])


val functionalTestTask = tasks.register("functionalTestTask") {
    dependsOn(testing.suites.named("functionalTest"))
}

tasks.check {
    // Include functionalTest as part of the check which implicitly means build lifecycle
    dependsOn(functionalTestTask)
}
