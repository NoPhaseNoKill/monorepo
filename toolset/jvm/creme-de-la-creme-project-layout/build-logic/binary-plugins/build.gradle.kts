

plugins {
    kotlin("jvm") // Use the appropriate Kotlin version
    `java-gradle-plugin`
}

group = "com.nophasenokill"
version = "1.0-SNAPSHOT"

gradlePlugin {
    plugins {
        create("javaBasePlugin") {
            id = "${group}.java-base-plugin"
            implementationClass = "JavaBasePlugin"
        }
    }
}

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
}

val lines = file("../../kotlin-meta-dependencies.txt").readLines()
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
