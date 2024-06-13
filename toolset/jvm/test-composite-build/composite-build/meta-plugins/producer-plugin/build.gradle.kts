plugins {
    id("com.nophasenokill.standalone-kotlin-base-plugin")
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("producer-plugin") {
            id = "com.nophasenokill.producer-plugin"
            implementationClass = "com.nophasenokill.ProducerPlugin"
        }
    }
}

dependencies {

}