plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(project(":base-plugin"))

    // includes junit plugin which includes test deps etc
    // implementation(project(":application-plugin"))

    implementation(project(":application-plugin"))
    implementation(project(":my-kotlin-plugin"))
    implementation(project(":junit-test-plugin"))
    // implementation(project(":source-file-hashing-plugin"))
    // implementation(project(":capability-conflict-avoidance-plugin"))
    // implementation(project(":dependency-analysis-project"))
    // implementation(project(":tested-plugins"))
}
