plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("publishing-plugin") {
            id = "com.nophasenokill.publishing-plugin"
            implementationClass = "com.nophasenokill.PublishingPlugin"
        }
    }
}

dependencies {

}

group = "com.nophasenokill"
version = "1.0.0-local-dev"

