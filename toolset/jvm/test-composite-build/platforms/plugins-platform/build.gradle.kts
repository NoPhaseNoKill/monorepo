plugins {
    id("java-platform")
}

group = "com.nophasenokill.platform"

dependencies {
    constraints {
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.20")
        api("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
    }
}