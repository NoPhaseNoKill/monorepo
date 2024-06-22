plugins {
    id("com.nophasenokill.base-kotlin-plugin")
    id("java-library")
}

gradlePlugin {
    plugins {
        create("consumer-kotlin-plugin") {
            id = "com.nophasenokill.consumer-kotlin-plugin"
            implementationClass = "com.nophasenokill.ConsumerKotlinPlugin"
        }
    }
}

dependencies {
    implementation("com.nophasenokill:base-kotlin-plugin")
}