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
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
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
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.22")
    testImplementation(gradleTestKit())
}

tasks.test {
    useJUnitPlatform()

    testLogging.setShowStandardStreams(true)
    testLogging.minGranularity = 2
}

testing {
    suites {

        fun applySharedJvmTestDependencies(dependencies: JvmComponentDependencies) {
            with(dependencies) {

                implementation(platform("org.junit:junit-bom:5.10.1"))
                implementation("org.junit.jupiter:junit-jupiter")
                runtimeOnly("org.junit.platform:junit-platform-launcher")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.0")
            }
        }


        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.10.1")

            dependencies {
                applySharedJvmTestDependencies(this)
            }
        }

        val functionalTest by registering(JvmTestSuite::class) {
            useJUnitJupiter("5.10.1")

            dependencies {
                // functionalTest test suite depends on the production code in tests
                implementation(project())
                applySharedJvmTestDependencies(this)
            }
        }
    }
}

gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])

tasks.named<Task>("check") {
    // Include functionalTest as part of the check which implicitly means build lifecycle
    dependsOn(testing.suites.named("functionalTest"))
}
