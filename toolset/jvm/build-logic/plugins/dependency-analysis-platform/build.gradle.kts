println("Initializing build.gradle.kts for: $name")

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    // requires version for current implementation, ideally we should have central repo for declaration of versions (maybe toml)
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:1.28.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
}
