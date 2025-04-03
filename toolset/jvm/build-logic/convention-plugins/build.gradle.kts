
plugins {
    `kotlin-dsl`
}

group = "com.nophasenokill.$name"
version = "0.1.local-dev"

repositories.gradlePluginPortal()


dependencies {
    /*
        Equivalent of: implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${libs.versions.kotlin.get()}")
     */
    implementation(kotlin("gradle-plugin", libs.versions.kotlin.get()))
    implementation("org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:${libs.versions.kotlinDsl.get()}")
}
