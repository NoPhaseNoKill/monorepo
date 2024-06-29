

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    id("com.nophasenokill.build-service-warning-fix-plugin")
}

group = "com.nophasenokill.hash-source-plugin"
version = "0.1.local-dev"

gradlePlugin {
    val hashSourcePlugin by plugins.creating {
        id = "com.nophasenokill.hash-source-plugin"
        implementationClass = "com.nophasenokill.HashSourcePlugin"
    }
}

dependencies {
    // includes both person and account and org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.22
    implementation("com.nophasenokill.domain:person:0.1.local-dev")
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