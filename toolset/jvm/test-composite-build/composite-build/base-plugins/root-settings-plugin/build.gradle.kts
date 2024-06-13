
plugins {
    `kotlin-dsl`
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.gradle.toolchains:foojay-resolver:0.5.0")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}
