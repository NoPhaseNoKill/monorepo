plugins {
    id("base-plugin")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    // enforces that versions from each of the boms are used
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom"))
    testImplementation(enforcedPlatform("org.junit:junit-bom"))

    // applies standard kotlin libs to projects
    implementation(kotlin("stdlib-jdk8"))
    runtimeOnly(kotlin("reflect"))

    // applies test projects
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

kotlin {
    /*
        Uncomment this + the properties in gradle.properties to run using k2 compiler
     */
    // sourceSets.all {
    //     languageSettings {
    //         languageVersion = "2.0"
    //     }
    // }

    /*
        Uncomment this + targetCompatibility in the java extension if you need to verify
        that the default setting: jvmTargetValidationMode = JvmTargetValidationMode.ERROR
        is worked (default behaviour)
     */
    // compilerOptions {
    //     jvmTarget = JvmTarget.JVM_1_8
    // }
}



/*
    Ensures that jar is included properly for anyone who overrides destination folder
    See: https://kotlinlang.org/docs/gradle-configure-project.html#non-default-location-of-compile-tasks-destinationdirectory
 */

tasks.jar {
    from(sourceSets.main.get().output )
    from(sourceSets.main.get().kotlin.classesDirectory )
}

tasks.register("compileAll") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Compile all Java code"

    dependsOn(tasks.check)
}

tasks.register("printDependencies") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Prints dependencies by configuration"

    configurations.forEach { config ->
        if (config.isCanBeResolved) {
            logger.lifecycle("\nConfiguration dependencies: ${config.name}")
            try {
                config.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
                    logger.lifecycle(" - ${artifact.moduleVersion.id.group}:${artifact.name}:${artifact.moduleVersion.id.version}")
                }
            } catch (e: Exception) {
                logger.lifecycle("Failed to resolve ${config.name}: ${e.message}")
            }
        }
    }
}