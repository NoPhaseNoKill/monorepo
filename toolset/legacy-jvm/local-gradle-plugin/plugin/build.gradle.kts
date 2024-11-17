plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`
    // Allows us to publish locally
    `maven-publish`

    // Apply the Kotlin JVM plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)
}

version = "0.1.0"
group = "com.nophasenokill.greeting"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

gradlePlugin {

    // So we can use our custom task to publish
    isAutomatedPublishing = false

    val greeting by plugins.creating {
        id = "com.nophasenokill.greeting.build-plugin"
        implementationClass = "com.nophasenokill.LocalGradlePluginPlugin"
    }

    val settingsPlugin by plugins.creating {
        id = "com.nophasenokill.greeting.settings-plugin"
        implementationClass = "com.nophasenokill.LocalSettingsPlugin"
    }
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["functionalTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

gradlePlugin.testSourceSets.add(functionalTestSourceSet)

tasks.named<Task>("check") {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}

tasks.named<Test>("test") {
    // Use JUnit Jupiter for unit tests.
    useJUnitPlatform()
}

fun MavenPublication.configureGradleModulePublication(pluginName: String) {
    groupId = "com.nophasenokill.greeting"
    artifactId = "com.nophasenokill.greeting.${pluginName}.gradle.plugin"
    version = "0.1.0"

    versionMapping {
        usage("java-api") {
            fromResolutionOf("runtimeClasspath")
        }
        usage("java-runtime") {
            fromResolutionResult()
        }
    }

    pom {
        groupId = "com.nophasenokill.greeting.${pluginName}"
        artifactId = "com.nophasenokill.greeting.${pluginName}.gradle.plugin"
        version = "0.1.0"
    }

    pom.withXml {
        val root = asNode()
        root.appendNode("dependencies").apply {
            appendNode("dependency").apply {
                appendNode("groupId", "com.nophasenokill.greeting")
                appendNode("artifactId", "plugin")
                appendNode("version", "0.1.0")
            }
        }
    }
}


afterEvaluate {
    val publishingExtension = extensions.findByType<PublishingExtension>()
    publishingExtension?.publications {

        create<MavenPublication>("greetingPluginMarkerMaven") {
            configureGradleModulePublication("build-plugin")
        }

        create<MavenPublication>("settingsPluginPluginMarkerMaven") {
            configureGradleModulePublication("settings-plugin")
        }

        create<MavenPublication>("mavenJava") {

            from(components["java"])

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            groupId = "com.nophasenokill.greeting"
            artifactId = "plugin"
            version = "0.1.0"

            pom.withXml {

                /*
                    Ensures no new lines, and avoids having to try and get around the monstrosity of xml
                 */

                this.asString().replace(0, this.asString().length, """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.nophasenokill.greeting</groupId>
  <artifactId>plugin</artifactId>
  <version>0.1.0</version>
  <dependencies>
    <dependency>
      <groupId>org.jetbrains.kotlin</groupId>
      <artifactId>kotlin-stdlib</artifactId>
      <version>${libs.versions.kotlin.get()}</version>
      <scope>compile</scope>
    </dependency>
  </dependencies>
</project>""".trimIndent())
            }
        }
        repositories {
            maven {
                println("Root dir: ${rootProject.rootDir}")
                val file = rootProject.rootDir
                println("Resolved local repo for published: ${uri(file.resolve("local-repo"))}")
                url = uri(file.resolve("local-repo"))
            }
        }
    }
}

tasks.register("publishToCustomLocalRepo") {
    dependsOn("publishToMavenLocal")
}

tasks.build {
    dependsOn("publishToCustomLocalRepo")
}
