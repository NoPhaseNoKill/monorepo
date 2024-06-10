
plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.23"))
    implementation(project(":producerPlugin"))

    configurations.all {
        isTransitive = false
    }
}

/*

    This highlights the non-transitive dependencies of the consumer/producer.

    Please note that the only difference (and rightly so), is that the consumer
    plugin has a single, individual added dependency: the producer plugin.

    producerPlugin dependencies:

        org.jetbrains.kotlin:kotlin-assignment-compiler-plugin-embeddable:1.9.22
        org.jetbrains.kotlin:kotlin-bom:1.9.23
        org.jetbrains.kotlin:kotlin-build-tools-impl:1.9.22
        org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.22
        org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23
        org.jetbrains.kotlin:kotlin-klib-commonizer-embeddable:1.9.22
        org.jetbrains.kotlin:kotlin-reflect:1.9.22
        org.jetbrains.kotlin:kotlin-sam-with-receiver-compiler-plugin-embeddable:1.9.22
        org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:1.9.22
        org.jetbrains.kotlin:kotlin-stdlib:1.9.22


    consumerPlugin dependencies:

        producerPlugin <---- ****Notice this here***
        org.jetbrains.kotlin:kotlin-assignment-compiler-plugin-embeddable:1.9.22
        org.jetbrains.kotlin:kotlin-bom:1.9.23
        org.jetbrains.kotlin:kotlin-build-tools-impl:1.9.22
        org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.22
        org.jetbrains.kotlin:kotlin-klib-commonizer-embeddable:1.9.22
        org.jetbrains.kotlin:kotlin-reflect:1.9.22
        org.jetbrains.kotlin:kotlin-sam-with-receiver-compiler-plugin-embeddable:1.9.22
        org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:1.9.22
        org.jetbrains.kotlin:kotlin-stdlib:1.9.22
 */



