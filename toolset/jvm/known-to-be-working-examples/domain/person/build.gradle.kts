
plugins {
    kotlin("jvm")
    id("java-library")
    `maven-publish`
}

group = "com.nophasenokill.domain"
version = "0.1.local-dev"

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.22")
    implementation(project(":account"))
}

publishing {
    repositories {
        maven {
            setUrl(file("../../../local-repo"))
        }
    }
    publications {
        create<MavenPublication>("maven") { from(components["java"]) }
    }
}