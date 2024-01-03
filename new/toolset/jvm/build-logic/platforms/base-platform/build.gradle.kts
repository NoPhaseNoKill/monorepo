plugins {
    id("java-platform")
}

group = "integraboost.platform"



dependencies {
    constraints {
        api("com.autonomousapps:dependency-analysis-gradle-plugin:1.28.0")
        api("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector:0.3")
        api("org.gradlex:java-ecosystem-capabilities:1.3.1")
    }
}
