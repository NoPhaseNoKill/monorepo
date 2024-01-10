plugins {
    `kotlin-dsl`
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(project(":kotlin-common-conventions"))

    // NOTE: If you want to have a shared plugin that includes plugins, DO NOT PUT IT HERE
    // it declares it on the whole project (aka kotlin-library-conventions)
    // The proper way is to specifically put it in the plugin itself

    // dependencies {
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
// }
}
