plugins {
    id("java-platform")
}

group = "com.nophasenokill.platforms"


// Do not use this. This includes the dependencies
// javaPlatform.allowDependencies()

dependencies {
    constraints {
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.21")
        api("org.slf4j:slf4j-api:2.0.12")
        runtime("org.slf4j:slf4j-simple:2.0.12")
    }
}
