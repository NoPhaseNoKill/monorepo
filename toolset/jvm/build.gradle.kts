plugins {
    kotlin("jvm") version "2.0.21"
    id("com.nophasenokill.exposed.java-version-checker-plugin")
    id("com.nophasenokill.exposed.plugin-base")
    id("com.nophasenokill.exposed.group-and-version-details-plugin")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
