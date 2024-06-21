
buildscript {
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("maven-repo"))
        }
        gradlePluginPortal()
        mavenCentral()
    }

    /*
        Using settingsEvaluated here ensures that if it is not yet published to your local build folder,
        it does so first
     */
    gradle.settingsEvaluated {
        dependencies {
            classpath("com.nophasenokill.library:com.nophasenokill.library.gradle.plugin:1.0.0")
        }
    }
}

/*
    This applies the plugin at for this project.
    Requires the above buildscript to be added, AND also the plugin to be published first,
    otherwise you will get random failures.

    Using settingsEvaluated here ensures that if it is not yet published to your local build folder,
    it does so first
 */
gradle.settingsEvaluated {
    apply(plugin = "com.nophasenokill.library")
}

plugins {
    `java-gradle-plugin`
    `maven-publish`
    `kotlin-dsl`
}

group = "com.nophasenokill"
version = "1.0.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.22")
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

        create("producerConfigurationPlugin") {
            id = "com.nophasenokill.producer"
            implementationClass = "com.nophasenokill.ProducerConfigurationPlugin"
        }

        create("consumerConfigurationPlugin") {
            id = "com.nophasenokill.consumer"
            implementationClass = "com.nophasenokill.ConsumerConfigurationPlugin"
        }
    }
}