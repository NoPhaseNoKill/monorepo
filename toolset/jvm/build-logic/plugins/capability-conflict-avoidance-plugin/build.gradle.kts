

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(project(":base-plugin"))

    // required to compile the plugin, and hence implicitly relies on the platform being declared
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector")
    implementation("org.gradlex:java-ecosystem-capabilities")
}