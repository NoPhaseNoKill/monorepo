

plugins {
    `kotlin-dsl`
}



configurations.all {
    resolutionStrategy {
        failOnVersionConflict()
        /*
            equivalent to both:
                - failOnDynamicVersions()
                - failOnChangingVersions()
         */
        failOnNonReproducibleResolution()
    }
}

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))

    implementation(project(":base-plugin"))
    implementation(project(":capability-conflict-avoidance-plugin"))

    implementation("com.autonomousapps:dependency-analysis-gradle-plugin")

    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
}
