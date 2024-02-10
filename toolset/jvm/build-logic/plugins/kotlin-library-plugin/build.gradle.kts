
plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))

    implementation(project(":dependency-analysis-project"))
    implementation(project(":capability-conflict-avoidance-plugin"))
}
