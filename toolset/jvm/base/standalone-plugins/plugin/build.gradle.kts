plugins {
    /*
        This should be the only place referencing this. Ideally it should be moved into
        its own meta-plugin folder to create complete independence/isolation and expose
        com.nophasenokill.kotlin-base-plugin solely
     */
    alias(libs.plugins.kotlinJvm)
    `java-gradle-plugin`
}

repositories {
    gradlePluginPortal()
}

gradlePlugin {

    val greeting by plugins.creating {
        id = "com.nophasenokill.greeting"
        implementationClass = "com.nophasenokill.GreetingPlugin"
        version = "0.1.local-dev"
    }

    val kotlinBase by plugins.creating {
        id = "com.nophasenokill.kotlin-base-plugin"
        implementationClass = "com.nophasenokill.KotlinBasePlugin"
        version = "0.1.local-dev"
    }
}

dependencies {


    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${libs.versions.kotlin.get()}")

    testImplementation(platform("org.junit:junit-bom:${libs.versions.junit.get()}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit)
        }

        // Create a new test suite
        val functionalTest by registering(JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit)

            dependencies {
                // functionalTest test suite depends on the production code in tests
                implementation(project())
            }
        }
    }
}

gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])

tasks.named<Task>("check") {
    // Include functionalTest as part of the check lifecycle
    dependsOn(testing.suites.named("functionalTest"))
}