plugins {
    `java-gradle-plugin`
    `maven-publish`
    `kotlin-dsl` // allows for actually writing the kotlin code in the src/main/kotlin folder
    id("com.nophasenokill.library") version("1.0.0")
    id("com.nophasenokill.consumer") version("1.0.0")
    id("com.nophasenokill.producer") version("1.0.0")
}

group = "com.nophasenokill"
version = "1.0.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("metaPlugin") {
            id = "com.nophasenokill.meta-plugin"
            implementationClass = "com.nophasenokill.MetaPlugin"
        }
    }
}

dependencies {
    implementation("com.nophasenokill:standalone-plugin")
}