plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:plugins-platform"))

    implementation(project(":commons-plugin"))
    implementation("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector")
    implementation("org.gradlex:java-ecosystem-capabilities")
}