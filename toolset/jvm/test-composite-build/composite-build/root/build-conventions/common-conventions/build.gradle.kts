plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("common-conventions") {
            id = "com.nophasenokill.common-conventions"
            implementationClass = "com.nophasenokill.CommonConventionsPlugin"
        }
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"
