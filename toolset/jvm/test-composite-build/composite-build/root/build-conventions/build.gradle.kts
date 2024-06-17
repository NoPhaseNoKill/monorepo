plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("build-conventions") {
            id = "com.nophasenokill.build-conventions"
            implementationClass = "com.nophasenokill.BuildConventionsPlugin"
        }
    }
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"

dependencies {
    implementation("com.nophasenokill:common-conventions")
    implementation("com.nophasenokill:java-conventions")
}
