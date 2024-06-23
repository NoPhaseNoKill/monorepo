plugins {
    id("java-platform")
}

group = "com.nophasenokill.platform"

dependencies {
    constraints {
        api("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.8.1")
    }
}