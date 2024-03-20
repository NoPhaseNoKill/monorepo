

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
    // platform hasn't been loaded yet here, and hence these are required to compile the plugin
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:${libs.versions.kotlin.get()}"))
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:${libs.versions.dependencyAnalysisGradlePlugin.get()}") {
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
