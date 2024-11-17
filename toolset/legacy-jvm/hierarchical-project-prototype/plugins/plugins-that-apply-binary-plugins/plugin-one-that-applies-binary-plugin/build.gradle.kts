
plugins {
    kotlin("jvm") // required for dependencies.implementation below
    /*
       can't use alias due to already being on classpath, but leaving here to test that we can import alias,
        required for dependencies.implementation below
     */
    println("libs.plugins.kotlinJvm is: ${libs.plugins.kotlinJvm}")
    id("com.nophasenokill.binary-plugins.binary-plugin-one")
    id("com.nophasenokill.binary-plugins.binary-plugin-two")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ensures declared version is used rather than the default
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    implementation("com.nophasenokill.binary-plugins:binary-plugin-one:0.1.local-dev")
    implementation("com.nophasenokill.binary-plugins:binary-plugin-two:0.1.local-dev")
}
