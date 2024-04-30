
group = "com.nophasenokill.applications"
version = "0.1.local-dev"

plugins {
    kotlin("jvm") version("1.9.21")
    id("com.nophasenokill.meta-plugins.pin-kotlin-dependency-versions-plugin")
    id("com.nophasenokill.meta-plugins.check-kotlin-build-service-fix-plugin")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(platform("com.nophasenokill.platforms:generalised-platform"))
    implementation("com.nophasenokill.standalone-plugins:standalone-plugin-one")
    implementation("com.nophasenokill.meta-plugins:meta-plugin-one")
}




