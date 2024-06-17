plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("java-conventions") {
            id = "com.nophasenokill.java-conventions"
            implementationClass = "com.nophasenokill.JavaConventionsPlugin"
        }
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("com.nophasenokill:common-conventions")
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"
