

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))

    // implementation(project(":commons-plugin"))
    implementation(project(":capability-conflict-avoidance-plugin"))
    implementation(project(":dependency-analysis-project"))
    // implementation(project(":tested-plugins"))


}
