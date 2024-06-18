
buildscript {
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("maven-repo"))
        }
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.nophasenokill.library:com.nophasenokill.library.gradle.plugin:1.0.0")
    }
}

/*
    This applies the plugin at for this project.
    Requires the above buildscript to be added, AND also the plugin to be published first,
    otherwise you will get random failures.
 */
apply(plugin = "com.nophasenokill.library")

plugins {
    `java-gradle-plugin`
    `maven-publish`
    `kotlin-dsl` // allows for actually writing the kotlin code in the src/main/kotlin folder
}

group = "com.nophasenokill"
version = "1.0.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("applicationPlugin") {
            id = "com.nophasenokill.application"
            implementationClass = "com.nophasenokill.ApplicationPlugin"
        }
        create("libraryPlugin") {
            id = "com.nophasenokill.library"
            implementationClass = "com.nophasenokill.LibraryPlugin"
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("maven-repo"))
        }
    }
}