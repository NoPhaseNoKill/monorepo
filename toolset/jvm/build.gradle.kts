plugins {
    `kotlin-dsl` apply false
    id("com.autonomousapps.dependency-analysis") version "1.28.0"
}

dependencyAnalysis {
    issues {
        // configure for all projects
        all {
            // set behavior for all issue types
            onAny {
                severity("fail")
            }
        }
    }
}