

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
    /*
        Because this is a requirement to compile our full app platform (it's doing a pre-check of our platform),
        currently versions need to be declared manually here. this is currently a workaround, ideally we
        should be storing these in a .toml file for consistency
     */
    implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:1.9.20"))
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
