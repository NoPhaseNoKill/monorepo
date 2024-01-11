plugins {
    id("java-platform")
}

group = "com.nophasenokill.platform"

javaPlatform.allowDependencies()

dependencies {
    api(platform("org.jetbrains.kotlin:kotlin-bom:1.9.20"))

    constraints {
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.20")
        api("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
        api("org.gradle.toolchains:foojay-resolver:0.7.0")
        api("com.gradle.enterprise:com.gradle.enterprise.gradle.plugin:3.16.1")
        api( "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    }
}