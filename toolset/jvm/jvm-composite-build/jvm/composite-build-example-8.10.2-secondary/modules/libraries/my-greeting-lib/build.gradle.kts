
buildscript {
    repositories {
        maven {
            url = uri(rootDir.resolve("modules/plugins/greeting-plugin/build/repo"))
        }
        gradlePluginPortal()
    }

    dependencies {
        classpath("org.sample.greeting:org.sample.greeting.gradle.plugin:1.0-SNAPSHOT")
    }
}

plugins {
    kotlin("jvm") version "2.0.20"
    id("java-library")
    id("org.sample.greeting")
    // id("org.sample.greeting").version("1.0-SNAPSHOT")
    // id("org.sample.secondary").version("1.0-SNAPSHOT")
}

// configurations.all {
//     println("Configuration: ${this.name} for: ${project.name}")
//     resolutionStrategy.dependencySubstitution {
//         substitute(module("org.sample.greeting:org.sample.greeting.gradle.plugin"))
//             .using(project(":modules:plugins:greeting-plugin"))
//
//         all {
//
//             val requested = this.requested
//             if (requested is ModuleComponentSelector) {
//                 println("${requested.group}:${requested.module}:${requested.version}")
//             } else {
//                 println("Requested: ${this.requested.requestedCapabilities}")
//             }
//
//
//         }
//         // println("Requested: ${this}")
//         // substitute(module("org.sample:greeting-plugin"))
//         //     .using(project(":modules:plugins:greeting-plugin")).because("we work with the unreleased development version")
//     }
// }

repositories {
    gradlePluginPortal()
}

// greeting {
//     who = "Bob"
// }
//
// greetingSecondary {
//     who = "Bob2"
// }
