

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
    implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:1.9.20"))

    implementation(platform("com.nophasenokill.platform:platform"))

    implementation(project(":capability-conflict-avoidance-plugin"))

    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")

    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:1.29.0") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-common")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-scripting-common")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-scripting-jvm")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-daemon-client")
        exclude(group = "org.checkerframework", module = "checker-qual")
        exclude(group = "com.google.errorprone", module = "error_prone_annotations")
    }

}
