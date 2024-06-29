plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.nophasenokill.root-settings-plugin"
version = "0.1.local-dev"

gradlePlugin {
    val rootSettingsPlugin by plugins.creating {
        id = "com.nophasenokill.root-settings-plugin"
        implementationClass = "com.nophasenokill.RootSettingsPlugin"
    }
}

publishing {
    repositories {
        maven {
            setUrl(file("../../../local-repo"))
        }
    }
    publications {
        create<MavenPublication>("maven") { from(components["java"]) }
    }
}