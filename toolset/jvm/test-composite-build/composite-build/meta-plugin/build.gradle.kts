plugins {
    `java-gradle-plugin`
    `maven-publish`
    `kotlin-dsl` // allows for actually writing the kotlin code in the src/main/kotlin folder
    id("com.nophasenokill.library") version("1.0.0")
}

group = "com.nophasenokill"
version = "1.0.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("com.nophasenokill:standalone-plugin")
}

gradlePlugin {
    plugins {
        create("metaPlugin") {
            id = "com.nophasenokill.meta-plugin"
            implementationClass = "com.nophasenokill.MetaPlugin"
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