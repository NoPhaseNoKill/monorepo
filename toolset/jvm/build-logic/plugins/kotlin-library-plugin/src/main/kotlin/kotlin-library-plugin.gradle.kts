plugins {
    id("commons-plugin")
    id("java-library")
    id("dependency-analysis-project")
    id("greeting-plugin")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}
