plugins {
    id("java-platform")
}

group = "com.nophasenokill.platforms"


// Do not use this. This includes the dependencies
// javaPlatform.allowDependencies()

dependencies {
    constraints {
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.21")
    }
}
