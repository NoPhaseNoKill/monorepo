
plugins {
    id("com.autonomousapps.dependency-analysis")
}

dependencyAnalysis {
    issues {
        all {
            onAny {
                severity("fail")
                exclude("jakarta.activation:jakarta.activation-api", "javax.activation:activation")
            }
        }
    }
}