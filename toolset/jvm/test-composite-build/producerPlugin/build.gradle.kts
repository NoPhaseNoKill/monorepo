plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.9.23"
}

gradlePlugin {
    plugins {
        create("producerPlugin") {
            id = "com.nophasenokill.producer"
            implementationClass = "com.nophasenokill.ProducerPlugin"
        }
    }
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

version = "1.0.0-local-dev"
group = "com.nophasenokill"


tasks.test {
    useJUnitPlatform()
}