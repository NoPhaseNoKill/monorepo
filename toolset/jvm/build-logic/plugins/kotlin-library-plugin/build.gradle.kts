

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:platform"))

    implementation(project(":commons-plugin"))
    implementation(project(":tested-plugins"))
    implementation(project(":dependency-analysis-project"))

    implementation("com.autonomousapps:dependency-analysis-gradle-plugin")
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
}
