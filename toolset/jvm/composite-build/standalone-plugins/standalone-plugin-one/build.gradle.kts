plugins {
    // id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm")
    `java-gradle-plugin` // also known as id("com.gradle.plugin-publish") version "1.2.1"
}


group = "com.nophasenokill.standalone-plugins"

repositories {
    gradlePluginPortal()
}

gradlePlugin {
    val javaBase by plugins.creating {
        id = "com.nophasenokill.java-base-plugin"
        implementationClass = "com.nophasenokill.JavaBasePlugin"
        version = "0.1.local-dev"
    }

    val kotlinBase by plugins.creating {
        id = "com.nophasenokill.kotlin-base-plugin"
        implementationClass = "com.nophasenokill.KotlinBasePlugin"
        version = "0.1.local-dev"
    }

    val kotlinApplication by plugins.creating {
        id = "com.nophasenokill.kotlin-application-plugin"
        implementationClass = "com.nophasenokill.KotlinApplicationPlugin"
        version = "0.1.local-dev"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.22")
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
