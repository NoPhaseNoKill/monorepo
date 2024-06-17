plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("root-settings-plugin") {
            id = "com.nophasenokill.root-settings-plugin"
            implementationClass = "com.nophasenokill.RootSettingsPlugin"
        }
    }
}