plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

// Ensures declared version is used rather than the default
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
}
