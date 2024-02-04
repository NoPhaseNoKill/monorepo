

plugins {
    `kotlin-dsl`
}

configurations.all {
    resolutionStrategy {
        // force("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
        force("org.jetbrains.kotlin:kotlin-reflect:1.9.20")
        failOnVersionConflict()
    }
}

dependencies {
    // requires version for current implementation, ideally we should have central repo for declaration of versions (maybe toml)

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
