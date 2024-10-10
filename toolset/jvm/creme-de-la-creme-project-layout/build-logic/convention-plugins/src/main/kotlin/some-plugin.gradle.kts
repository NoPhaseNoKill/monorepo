
plugins {
    kotlin("jvm")
}

group = "com.nophasenokill"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val lines = file("../kotlin-meta-dependencies.txt").readLines()
val kotlinVersion = lines.first { it.contains("kotlinJvmVersion") }.substringAfter("=").trimStart()
val foojayResolverVersion = lines.first { it.contains("fooJayResolverVersion") }.substringAfter("=").trimStart()


dependencies {

    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${kotlinVersion}")
    implementation("org.gradle.toolchains.foojay-resolver-convention:org.gradle.toolchains.foojay-resolver-convention.gradle.plugin:${foojayResolverVersion}")


    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
