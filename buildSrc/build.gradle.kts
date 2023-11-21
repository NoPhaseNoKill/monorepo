plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    val gradlePluginVersion = "1.8.10"
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$gradlePluginVersion")
}