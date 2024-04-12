plugins {
    id("com.nophasenokill.kotlin-application-plugin")
}

group = "com.nophasenokill"
version = "1.0-SNAPSHOT"

dependencies {
    /*
        Adds in the dependency for a plugin we rely on
     */
    implementation("com.nophasenokill.standalone-plugins:standalone-plugin-one")
}