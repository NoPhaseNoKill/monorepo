

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))

    implementation(project(":commons-plugin"))
    implementation(project(":capability-conflict-avoidance-plugin"))

    implementation("com.autonomousapps:dependency-analysis-gradle-plugin")

    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
}
