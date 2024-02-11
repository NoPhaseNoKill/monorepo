
plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))

    implementation(project(":library-plugin"))
    implementation(project(":my-kotlin-plugin"))
    implementation(project(":junit-test-plugin"))
    implementation(project(":dependency-analysis-project"))
    implementation(project(":capability-conflict-avoidance-plugin"))
    implementation(project(":tested-plugins"))
}