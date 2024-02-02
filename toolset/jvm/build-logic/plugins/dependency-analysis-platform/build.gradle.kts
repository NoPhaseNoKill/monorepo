

plugins {
    `kotlin-dsl`
}

dependencies {
    // requires version for current implementation, ideally we should have central repo for declaration of versions (maybe toml)
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:1.29.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
}
