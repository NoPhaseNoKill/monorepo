plugins {
    `kotlin-dsl` apply false
    id("com.autonomousapps.dependency-analysis") version "1.28.0"
}

dependencyAnalysis {
    issues {
        all {
            onAny {
                severity("fail")
            }
        }
    }
}