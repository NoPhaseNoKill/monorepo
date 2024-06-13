plugins {
    id("com.nophasenokill.standalone-kotlin-base-plugin")
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
    implementation("com.nophasenokill:producer-plugin:1.0.0-local-dev")
}

tasks.build {
    dependsOn(":producer-plugin:build")
    mustRunAfter(":producer-plugin:build")
}

