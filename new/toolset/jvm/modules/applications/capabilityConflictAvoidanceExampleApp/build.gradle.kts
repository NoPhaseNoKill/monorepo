plugins {
    id("integraboost.plugin.module.java-project-plugin")
    id("application")
}

application {
    mainClass.set("integraboost.App")
}

dependencies {
    implementation("javax.activation:activation:1.1.1")
    implementation("javax.activation:activation:1.1")
    implementation("jakarta.activation:jakarta.activation-api:1.2.2")
}