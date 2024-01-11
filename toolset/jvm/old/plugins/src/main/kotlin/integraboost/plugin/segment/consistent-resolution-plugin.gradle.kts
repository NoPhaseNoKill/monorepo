package integraboost.plugin.segment

plugins {
    id("integraboost.plugin.segment.base-plugin")
}

java {
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