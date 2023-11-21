
plugins {
    id("org.jetbrains.kotlin.jvm")
    id("idea")
}

group = "com.integraboost"
version = "1.0.0"
buildDir = File("${rootProject.buildDir}/${project.name}")

repositories {
    mavenCentral()
}

kotlin {
    val javaVersion = JavaVersion.VERSION_17
    jvmToolchain(javaVersion.toString().toInt())
}

dependencies {
    val kotlinCoroutinesVersion = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinCoroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${kotlinCoroutinesVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
