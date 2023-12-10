plugins {
    id("com.integraboost.commons")
    id("org.jetbrains.kotlin.jvm")
    id("java-library")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(kotlin("stdlib"))
}
