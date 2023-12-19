plugins {
    id("java")
    id("idea")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    // Enforces consistent resolution so that we don't end up with tests running against a different version than our app
    // See: https://docs.gradle.org/current/userguide/resolution_strategy_tuning.html#sec:java_consistency
    consistentResolution {
        useCompileClasspathVersions()
    }
}

// Another mechanism for trying to control consistent resolution. See: https://docs.gradle.org/current/userguide/resolution_strategy_tuning.html#reproducible-resolution
configurations.all {
    resolutionStrategy {
        failOnNonReproducibleResolution()
    }
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
