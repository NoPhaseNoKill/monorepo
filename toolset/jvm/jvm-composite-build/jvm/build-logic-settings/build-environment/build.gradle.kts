import com.gradle.develocity.agent.gradle.DevelocityConfiguration

plugins {
    `kotlin-dsl`
}

description = "Provides plugins for configuring build environment"

group = "com.nophasenokill"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        vendor = JvmVendorSpec.ADOPTIUM
    }
}

dependencies {
    implementation("com.gradle:develocity-gradle-plugin:3.18.1")
}

