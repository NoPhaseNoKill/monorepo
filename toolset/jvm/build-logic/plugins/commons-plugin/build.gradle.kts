println("Initializing build.gradle.kts for: $name")

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:platform"))

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
}