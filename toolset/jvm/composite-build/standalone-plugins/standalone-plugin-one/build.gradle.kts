import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.internal.impldep.org.junit.platform.engine.discovery.DiscoverySelectors
import org.gradle.internal.impldep.org.junit.platform.engine.reporting.ReportEntry
import org.gradle.internal.impldep.org.junit.platform.launcher.Launcher
import org.gradle.internal.impldep.org.junit.platform.launcher.TestExecutionListener
import org.gradle.internal.impldep.org.junit.platform.launcher.TestIdentifier
import org.gradle.internal.impldep.org.junit.platform.launcher.TestPlan
import org.gradle.internal.impldep.org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.gradle.internal.impldep.org.junit.platform.launcher.core.LauncherFactory

plugins {
    // id("java-gradle-plugin")
    `java-gradle-plugin`

    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    // also known as id("com.gradle.plugin-publish") version "1.2.1"
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
}

dependencies {

    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.21"))
    implementation(platform("org.junit:junit-bom:5.10.1"))
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.8.0"))

    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.21") {
        exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core-jvm").because("It conflicts with coroutine BOM which expects 1.8.0 and this brings in 1.5.0")
    }

    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    testImplementation(gradleTestKit())
}

testing {
    suites {

        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.10.1")

            this.targets.configureEach {
                this.testTask.configure {
                    this.testLogging {
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

tasks.named<Task>("check") {
    // Include functionalTest as part of the check which implicitly means build lifecycle
    dependsOn(testing.suites.named("functionalTest"))
}
