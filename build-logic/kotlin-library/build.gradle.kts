plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.integraboost.build-logic-platform:plugins-platform"))

    implementation(project(":commons"))
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
}