
plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("kotlin-dsl-plugin") {
            id = "com.nophasenokill.kotlin-dsl-plugin"
            implementationClass = "com.nophasenokill.KotlinDslPlugin"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${embeddedKotlinVersion}")
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"