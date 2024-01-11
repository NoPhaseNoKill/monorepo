plugins {
    // Support convention plugins written in Kotlin. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    `kotlin-dsl`
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.gradle.toolchains:foojay-resolver")
    implementation("com.gradle.enterprise:com.gradle.enterprise.gradle.plugin")
    implementation(platform("com.nophasenokill.platform:base-platform"))
}