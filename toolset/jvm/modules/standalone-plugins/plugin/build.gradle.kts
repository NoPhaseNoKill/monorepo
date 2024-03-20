
plugins {
    id("java-gradle-plugin" )
    alias(libs.plugins.kotlinJvm)
}


dependencies {

    testImplementation(platform("org.junit:junit-bom:${libs.versions.junit.get()}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

gradlePlugin {
    // Define the plugin
    val greeting by plugins.creating {
        id = "standalone-plugin"
        implementationClass = "com.nophasenokill.StandalonePlugin"
    }
}


testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        val functionalTestSuite = register<JvmTestSuite>("functionalTest") {
            dependencies {
                implementation(project())
                implementation(gradleTestKit())
            }

            // Prevents runner.withPluginClasspath from throwing InvalidPluginMetadataException
            configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
            configurations["functionalTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

        }

        gradlePlugin.testSourceSets.add(functionalTestSuite.get().sources)
    }
}


tasks.withType(JavaCompile::class.java).configureEach {
    enabled = false
}

tasks.check {
    dependsOn(testing.suites.named("functionalTest"))
}
