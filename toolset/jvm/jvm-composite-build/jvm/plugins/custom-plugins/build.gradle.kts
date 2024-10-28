plugins {
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.nophasenokill"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13")
}

gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = "com.nophasenokill.custom-plugins.greeting"
            implementationClass = "com.nophasenokill.CustomGreetingPlugin"
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}
