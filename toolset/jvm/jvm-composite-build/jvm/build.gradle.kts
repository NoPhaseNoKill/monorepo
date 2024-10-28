
buildscript {
    dependencies {
        classpath("com.nophasenokill.plugins:second-loaded:0.1.local-dev")
        classpath("com.nophasenokill.plugins:third-loaded:0.1.local-dev")
    }
}

plugins {
    id("second-loaded-plugin")
    id("third-loaded-plugin")
    id("java")
}

dependencies {
    implementation("com.nophasenokill.plugins:second-loaded:0.1.local-dev")
    implementation("com.nophasenokill.plugins:third-loaded:0.1.local-dev")
}
