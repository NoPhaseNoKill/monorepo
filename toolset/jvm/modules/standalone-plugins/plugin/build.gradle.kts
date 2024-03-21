
plugins {
    alias(libs.plugins.kotlinJvm)
    `java-gradle-plugin`
    `maven-publish`
}

gradlePlugin {
    plugins {
        val greeting by plugins.creating {
            id = "org.example.greeting"
            implementationClass = "org.example.GreetingPlugin"
            version = "0.1.local-dev"
        }
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

// plugins {
//     id("java-gradle-plugin" )
//     alias(libs.plugins.kotlinJvm)
// }
//
//
// dependencies {
//
//     testImplementation(platform("org.junit:junit-bom:${libs.versions.junit.get()}"))
//     testImplementation("org.junit.jupiter:junit-jupiter")
//     testRuntimeOnly("org.junit.platform:junit-platform-launcher")
// }
//
// gradlePlugin {
//     // Define the plugin
//     val greeting by plugins.creating {
//         id = "com.nophasenokill.standalone-plugins.plugin"
//         implementationClass = "com.nophasenokill.StandalonePlugin"
//     }
// }
//
//
testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit)
        }
//
//         val functionalTestSuite = register<JvmTestSuite>("functionalTest") {
//             useJUnitJupiter(libs.versions.junit)
//             dependencies {
//                 implementation(project())
//                 implementation(gradleTestKit())
//             }
//
//             // Prevents runner.withPluginClasspath from throwing InvalidPluginMetadataException
//             configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
//             configurations["functionalTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])
//
//         }
//
//         gradlePlugin.testSourceSets.add(functionalTestSuite.get().sources)
    }
}
//
//
// tasks.withType(JavaCompile::class.java).configureEach {
//     enabled = false
// }
//
// tasks.check {
//     dependsOn(testing.suites.named("functionalTest"))
// }
