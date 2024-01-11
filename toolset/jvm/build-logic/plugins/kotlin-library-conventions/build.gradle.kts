plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:base-platform"))
    implementation(project(":kotlin-base-conventions"))

    // NOTE: If you want to have a shared plugin that includes plugins, DO NOT PUT IT HERE
    // it declares it on the whole project (aka kotlin-library-conventions)
    // The proper way is to specifically put it in the plugin itself

    // dependencies {
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
// }
}
