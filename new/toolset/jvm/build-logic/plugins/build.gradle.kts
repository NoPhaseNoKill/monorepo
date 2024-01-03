plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("integraboost.platform:my-custom-platform"))

    implementation("com.autonomousapps:dependency-analysis-gradle-plugin")
    implementation("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector")
    implementation("org.gradlex:java-ecosystem-capabilities")
}