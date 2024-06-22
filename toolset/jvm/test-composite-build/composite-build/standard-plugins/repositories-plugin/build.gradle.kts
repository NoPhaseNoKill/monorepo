
plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("repositories-plugin") {
            id = "com.nophasenokill.repositories-plugin"
            implementationClass = "com.nophasenokill.RepositoriesPlugin"
        }
    }
}

dependencies {
    implementation("com.nophasenokill:kotlin-dsl-plugin")
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"