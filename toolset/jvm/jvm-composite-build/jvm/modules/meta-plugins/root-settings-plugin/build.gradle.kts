import java.nio.file.Paths

plugins {
    `java-gradle-plugin`
    id("org.gradle.maven-publish")
    id("org.jetbrains.kotlin.jvm") version ("2.1.0-Beta1")
}

kotlin {
    jvmToolchain(21)
}



group = "com.nophasenokill"
version = "1.0.0-local"

gradlePlugin {
    plugins {
        create("rootSettingsPlugin") {
            id = "com.nophasenokill.root-settings-plugin"
            implementationClass = "RootSettingsPlugin"
        }

        create("mavenLocalRepoPlugin") {
            id = "com.nophasenokill.maven-local-repo-plugin"
            implementationClass = "MavenLocalRepoPlugin"
        }
    }
    isAutomatedPublishing = false
}

repositories {
    gradlePluginPortal()
}

gradle.settingsEvaluated {
    buildscript {

        repositories {
            gradlePluginPortal()
            maven {
                // Determine the root project of the full build
                val rootProjectDir = if (gradle.parent == null) {
                    // This block means we're in the root build
                    project.rootDir
                } else {
                    // This block means We are in an included build, we need to get the top-level root project
                    gradle.parent?.rootProject?.rootDir
                }

                // Set the URL to point to the local-repo in the root project
                url =  uri(File(rootProjectDir, "local-repo"))
            }
        }

        dependencies {
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0-Beta1")
        }

        configurations.all {
            resolutionStrategy {
                force("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")
            }
        }
    }

    publishing.repositories {

        maven {
            // Determine the root project of the full build
            val rootProjectDir = if (gradle.parent == null) {
                // We are in the root build
                project.rootDir
            } else {
                // We are in an included build, get the top-level root project
                gradle.parent?.rootProject?.rootDir
            }

            // Set the URL to point to the local-repo in the root project
            url =  uri(File(rootProjectDir, "local-repo"))
        }
    }
}



subprojects {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")
        }
    }
}
publishing.publications.create<MavenPublication>("mavenJava") {
    from(components["java"])
    versionMapping { allVariants { fromResolutionResult() } }
}



// Because the jar is shared with the root build, this ensures we get a fresh copy each time the project changes
tasks.publish {
    dependsOn(tasks.jar)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1") {
        isTransitive = false
    }
    implementation("com.gradle:develocity-gradle-plugin:3.18.1") {
        isTransitive = false
    }
    implementation("org.gradle.toolchains:foojay-resolver:0.8.0") {
        isTransitive = false
    }
}

tasks.build {
    dependsOn(tasks.publish)
}
