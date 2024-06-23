plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    publishing
}

dependencies {
    implementation(platform("com.nophasenokill.platform:plugins-platform"))

    implementation("com.nophasenokill:commons")
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
}

kotlin {
    jvmToolchain(21)
}