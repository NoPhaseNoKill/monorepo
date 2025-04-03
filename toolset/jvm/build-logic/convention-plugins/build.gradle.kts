
plugins {
    `kotlin-dsl`
}

group = "com.nophasenokill.$name"
version = "0.1.local-dev"

repositories.gradlePluginPortal()


dependencies {
    /*
        Equivalent of: implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.0.21")
     */
    implementation(kotlin("gradle-plugin", "2.0.21"))
    implementation("org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:5.1.2")
}
