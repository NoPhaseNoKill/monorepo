

plugins {
    `kotlin-dsl`
}

group = "com.nophasenokill"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
}

val lines = file("../../../kotlin-meta-dependencies.txt").readLines()
val kotlinVersion = lines.first { it.contains("kotlinJvmVersion") }.substringAfter("=").trimStart()
val foojayResolverVersion = lines.first { it.contains("fooJayResolverVersion") }.substringAfter("=").trimStart()

dependencies {

    implementation(project.dependencies.create("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.20"))

    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${kotlinVersion}")
    implementation("org.gradle.toolchains.foojay-resolver-convention:org.gradle.toolchains.foojay-resolver-convention.gradle.plugin:${foojayResolverVersion}")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
