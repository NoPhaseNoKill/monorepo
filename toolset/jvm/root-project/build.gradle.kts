plugins {
    `java` // Apply Java plugin globally if needed
}

allprojects {
    repositories {
        mavenCentral()
    }

    // Enable build cache
    // gradle.startParameter.isBuildCacheEnabled = true
    // gradle.startParameter.isConfigurationCacheEnabled = true
}

subprojects {
    apply(plugin = "java")


}
