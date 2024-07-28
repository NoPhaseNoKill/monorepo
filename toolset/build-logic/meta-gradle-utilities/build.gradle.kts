plugins {
    alias(libs.plugins.kotlinJvm)
    id("java-library")
}

dependencies {
    implementation(gradleApi())
    implementation("org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:4.3.1")
}