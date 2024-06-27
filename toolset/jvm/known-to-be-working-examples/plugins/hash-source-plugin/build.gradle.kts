

plugins {
    `kotlin-dsl`
    id("java-library")
}

group = "com.nophasenokill.hash-source-plugin"
version = "0.1.local-dev"

gradlePlugin {
    val hashSourcePlugin by plugins.creating {
        id = "com.nophasenokill.hash-source-plugin"
        implementationClass = "com.nophasenokill.HashSourcePlugin"
    }
}

