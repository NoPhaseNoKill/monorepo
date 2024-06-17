plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("group-and-version-plugin") {
            id = "com.nophasenokill.group-and-version-plugin"
            implementationClass = "com.nophasenokill.GroupAndVersionPlugin"
        }
    }
}

dependencies {

}

group = "com.nophasenokill"
version = "1.0.0-local-dev"

