

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(project(":base-plugin"))

    // required to compile the plugin, and hence implicitly relies on the platform being declared
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation("commons-io:commons-io")
    implementation("commons-codec:commons-codec")
}


