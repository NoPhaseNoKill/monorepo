plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:base-platform"))
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
    implementation("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector")
    implementation("org.gradlex:java-ecosystem-capabilities")
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin")
}
