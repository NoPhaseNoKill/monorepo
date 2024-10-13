plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(21)
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    kotlin("stdlib")
    implementation("org.gradle.toolchains.foojay-resolver-convention:org.gradle.toolchains.foojay-resolver-convention.gradle.plugin:0.8.0")
    // implementation("com.gradle:develocity-gradle-plugin:3.18.1")
}
