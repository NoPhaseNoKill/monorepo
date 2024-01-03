plugins {
    id("java-platform")
}

group = "integraboost.platform"

dependencies {
    constraints {
        api("jakarta.activation:jakarta.activation-api:1.2.2")
        api("javax.activation:activation:1.1.1")
    }
}
