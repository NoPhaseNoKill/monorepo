plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    // TODO should this be here? Or is there a better way of doing this
    api("com.autonomousapps.dependency-analysis:com.autonomousapps.dependency-analysis.gradle.plugin:1.28.0")
}