plugins {
    id("commons-plugin")
    id("org.jetbrains.kotlin.jvm")
    id("java-library")
    id("dependency-analysis-project")
    id("greeting-plugin")
}

dependencies {
    implementation(kotlin("stdlib"))
}

