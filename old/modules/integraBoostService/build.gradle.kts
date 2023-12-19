plugins {
    id("com.integraboost.kotlin-application")
    id("org.jetbrains.kotlin.jvm")
}


dependencies {
    implementation(project(":integraBoostLibrary"))
    implementation("io.arrow-kt:arrow-core:1.2.0")
}
