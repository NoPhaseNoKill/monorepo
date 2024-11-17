plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("maven-publish")
}

group = "com.nophasenokill.${name}"
version = "0.1.local-dev"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
}

repositories {
    mavenCentral()
}

pluginManager.withPlugin("org.gradle.java-gradle-plugin") {
    gradlePlugin {
        // To add here
    }
}



