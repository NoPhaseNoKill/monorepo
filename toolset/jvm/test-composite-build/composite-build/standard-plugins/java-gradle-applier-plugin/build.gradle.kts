plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("java-gradle-applier-plugin") {
            id = "com.nophasenokill.java-gradle-applier-plugin"
            implementationClass = "com.nophasenokill.JavaGradleApplierPlugin"
        }
    }
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"