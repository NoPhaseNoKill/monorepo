
plugins {
    `kotlin-dsl`
}

group = "com.nophasenokill.$name"
version = "0.1.local-dev"

repositories.gradlePluginPortal()


dependencies {
    implementation("org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:5.1.2")
}
