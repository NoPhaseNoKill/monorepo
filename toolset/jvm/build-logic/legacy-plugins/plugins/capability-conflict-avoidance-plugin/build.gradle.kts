

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(project(":base-plugin"))
    implementation(project(":classpath-collision-detector-plugin"))

    // // required to compile the plugin, and hence implicitly relies on the platform being declared
    // implementation(platform("com.nophasenokill.platform:platform"))
}