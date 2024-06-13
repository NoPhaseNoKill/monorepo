

plugins {
    `maven-publish`
    `kotlin-dsl`
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"


dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${embeddedKotlinVersion}")
}

publishing {
    repositories {
        maven {
            url = uri("${rootProject.projectDir}/local-repo")
        }
    }
}

tasks.build {
    finalizedBy("publish")
}