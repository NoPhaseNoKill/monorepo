
plugins {
    id("com.nophasenokill.standalone-kotlin-base-plugin") version("1.0.0-local-dev")
    `kotlin-dsl`
    id("com.nophasenokill.producer-plugin") version("1.0.0-local-dev")
}


gradlePlugin {
    plugins {
        create("consumer-plugin") {
            id = "com.nophasenokill.consumer-plugin"
            implementationClass = "com.nophasenokill.ConsumerPlugin"
        }
    }
}

dependencies {
    implementation(project(":producer-plugin"))
    implementation(project(":standalone-kotlin-base-plugin"))
}