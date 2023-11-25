plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.integraboost.platform:plugins-platform"))

    implementation(projects.commons)
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
}
