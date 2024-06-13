plugins {
    id("com.nophasenokill.standalone-kotlin-base-plugin") version("1.0.0-local-dev")
    `kotlin-dsl`
}

project.group = "com.nophasenokill"
project.version = "1.0.0-local-dev"

gradlePlugin {
    plugins {
        create("producer-plugin") {
            id = "com.nophasenokill.producer-plugin"
            implementationClass = "com.nophasenokill.ProducerPlugin"
        }
    }
}

dependencies {
    implementation(project(":standalone-kotlin-base-plugin"))
}