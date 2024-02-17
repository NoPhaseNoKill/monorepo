

plugins {
    `kotlin-dsl`
}


dependencies {
    // required to compile the plugin, and hence implicitly relies on the platform being declared
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation("commons-io:commons-io")

    implementation("io.github.cdsap.talaiot:talaiot")
    implementation("io.github.cdsap.talaiot:io.github.cdsap.talaiot.gradle.plugin")
    implementation("com.google.code.gson:gson")
}
