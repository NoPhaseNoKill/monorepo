plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("maven-publish")
}

group = "com.nophasenokill.binary-plugins"
version = "0.1.local-dev"

gradlePlugin {
    val binaryPluginOne by plugins.creating {
        id = "com.nophasenokill.binary-plugins.binary-plugin-one"
        implementationClass = "BinaryPluginOne"
    }
}

repositories {
    mavenCentral()
}
// Ensures declared version is used rather than the default
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
}
