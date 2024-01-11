plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:plugins-platform"))

    implementation(project(":commons-plugin"))
    implementation(project(":capability-conflict-avoidance-plugin"))
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
}
