import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
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

                    maxParallelForks = Runtime.getRuntime().availableProcessors().div(2)
                    forkEvery = 1

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
