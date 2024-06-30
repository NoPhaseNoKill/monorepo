plugins {
    id("com.nophasenokill.basic-kotlin-plugin")
    application
}

java {
    application {
        mainClass.set("com.nophasenokill.ApplicationOneKt")
    }
}

dependencies {
    implementation(projects.libraryOne)
}