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
            id = "com.nophasenokill.non-meta-plugins.greeting"
            implementationClass = "com.nophasenokill.NonMetaGreetingPlugin"
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
