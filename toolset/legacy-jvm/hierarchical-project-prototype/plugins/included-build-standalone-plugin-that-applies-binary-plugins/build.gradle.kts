
plugins {
    alias(libs.plugins.kotlinJvm) // required for dependencies.implementation below
    id("com.nophasenokill.binary-plugins.binary-plugin-one")
    id("com.nophasenokill.binary-plugins.binary-plugin-two")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ensures declared version is used rather than the default
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    implementation("com.nophasenokill.binary-plugins:binary-plugin-one:0.1.local-dev")
    implementation("com.nophasenokill.binary-plugins:binary-plugin-two:0.1.local-dev")
}
