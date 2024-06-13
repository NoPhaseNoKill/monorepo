
plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${embeddedKotlinVersion}")
    implementation(gradleApi())
    testImplementation(gradleTestKit())
}