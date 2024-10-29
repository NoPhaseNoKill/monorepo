import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    kotlin("jvm")
    id("java-library")
    id("org.sample.greeting")
    // id("org.sample.greeting").version("1.0-SNAPSHOT")
    // id("org.sample.secondary").version("1.0-SNAPSHOT")
}

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
