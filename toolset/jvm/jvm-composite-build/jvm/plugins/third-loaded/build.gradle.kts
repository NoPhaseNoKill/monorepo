

buildscript {
    dependencies {
        classpath("com.nophasenokill.plugins:second-loaded:0.1.local-dev")
    }
}
plugins {
    `kotlin-dsl`
    id("second-loaded-plugin")
}

dependencies {
    implementation("com.nophasenokill.plugins:second-loaded:0.1.local-dev")
}
