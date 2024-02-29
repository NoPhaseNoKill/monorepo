plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    // enforces that versions from each of the boms are used
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom"))

    // applies standard kotlin libs to projects
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    runtimeOnly(kotlin("reflect"))
}

// used here because scoping means that it can't be used inside
val mainSourceSetOutputProvider = sourceSets.main.map { it.output }


kotlin {
    kotlinDaemonJvmArgs = listOf("-Xmx486m", "-Xms256m", "-XX:+UseParallelGC")

    /*
        Ensures that jar is included properly for anyone who overrides destination folder
        See: https://kotlinlang.org/docs/gradle-configure-project.html#non-default-location-of-compile-tasks-destinationdirectory
    */
    tasks.jar {

        val mainSourceSetKotlinClasses = sourceSets.main.map { it.kotlin.classesDirectory }

        from(mainSourceSetOutputProvider)
        from(mainSourceSetKotlinClasses)
    }

    //     /*
    //         Uncomment this + the properties in gradle.properties to run using k2 compiler
    //      */
    //     // sourceSets.all {
    //     //     languageSettings {
    //     //         languageVersion = "2.0"
    //     //     }
    //     // }
    //
    //     /*
    //         Uncomment this + targetCompatibility in the java extension if you need to verify
    //         that the default setting: jvmTargetValidationMode = JvmTargetValidationMode.ERROR
    //         is worked (default behaviour)
    //      */
    //     // compilerOptions {
    //     //     jvmTarget = JvmTarget.JVM_1_8
    //     // }
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