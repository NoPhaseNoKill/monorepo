plugins {
    id("com.integraboost.commons")
    application
}

group = "com.integraboost"

dependencies {
    implementation(kotlin("stdlib"))

    // TODO try and fix issue with 1.7.3, which relates to classpath -  potentially being caused by build process
    val kotlinCoroutinesVersion = "1.6.4"

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinCoroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${kotlinCoroutinesVersion}")
}