plugins {
    kotlin("jvm") version "1.9.23"
    id("com.nophasenokill.meta-plugins.meta-plugin-one")
}


dependencies {
    // runtimeOnly("org.slf4j:slf4j-simple:2.0.13") will error
    runtimeOnly("org.slf4j:slf4j-simple:2.0.11") // gets resolved to 2.0.12
    runtimeOnly("org.slf4j:slf4j-simple") // gets resolved to 2.0.12
}