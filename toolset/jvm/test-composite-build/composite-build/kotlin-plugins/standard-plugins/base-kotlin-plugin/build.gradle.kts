plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("base-kotlin-plugin") {
            id = "com.nophasenokill.base-kotlin-plugin"
            implementationClass = "com.nophasenokill.BaseKotlinPlugin"
        }
    }
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"

dependencies {
    implementation("com.nophasenokill:kotlin-dsl-plugin")
    implementation("com.nophasenokill:repositories-plugin")
    implementation("com.nophasenokill:group-and-version-plugin")
    implementation("com.nophasenokill:publishing-plugin")
    implementation("com.nophasenokill:java-gradle-applier-plugin")
    implementation("com.nophasenokill:root-settings-plugin")
}