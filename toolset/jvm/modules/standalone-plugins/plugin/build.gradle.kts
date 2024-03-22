
plugins {
    alias(libs.plugins.kotlinJvm)
    `java-gradle-plugin`
    `maven-publish`
}


gradlePlugin {
    val greeting by plugins.creating {
        id = "org.example.greeting"
        implementationClass = "com.nophasenokill.GreetingPlugin"
        version = "0.1.local-dev"
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

dependencies {
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

    gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])

    tasks.named<Task>("check") {
        // Include functionalTest as part of the check lifecycle
        dependsOn(testing.suites.named("functionalTest"))
    }
}