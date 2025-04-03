import com.nophasenokill.extensions.findCatalog
import com.nophasenokill.extensions.findCatalogVersion

plugins {
    kotlin("jvm")
    id("com.nophasenokill.idea-sources-download-plugin")
}

val versionCatalog = project.findCatalog()
val javaVersion = versionCatalog.findCatalogVersion("java")
val coroutinesVersion = versionCatalog.findCatalogVersion("coroutines")
val junitVersion = versionCatalog.findCatalogVersion("junit")
val junitPlatformVersion = versionCatalog.findCatalogVersion("junitPlatform")

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${coroutinesVersion}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter:${junitVersion}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${junitPlatformVersion}")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(javaVersion.toInt())
}
