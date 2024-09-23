plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("maven-publish")
}

group = "com.nophasenokill.binary-root-settings-plugin-one"
version = "0.1.local-dev"

gradlePlugin {
    val binaryPluginOne by plugins.creating {
        id = "com.nophasenokill.binary-root-settings-plugins.binary-root-settings-plugin-one"
        implementationClass = "BinaryRootSettingsPluginOne"
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

// Ensures declared version is used rather than the default
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    implementation("com.gradle:develocity-gradle-plugin:${libs.versions.develocity.get()}")
}
