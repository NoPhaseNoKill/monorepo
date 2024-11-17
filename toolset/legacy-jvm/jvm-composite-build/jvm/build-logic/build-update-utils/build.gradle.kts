plugins {
    id("com.nophasenokill.build-logic.kotlin-dsl-gradle-plugin")
    id("com.nophasenokill.build-logic.groovy-dsl-gradle-plugin")
}

description = "Provides plugins that create update tasks for the Gradle build"

dependencies {
    implementation("com.nophasenokill:basics")
    implementation("com.google.code.gson:gson")
    implementation("org.jsoup:jsoup")
}
