

plugins {
    `kotlin-dsl`
}


dependencies {
    implementation("commons-io:commons-io:${libs.versions.commonsIo.get()}")

    implementation("io.github.cdsap.talaiot:talaiot:${libs.versions.talaiot.get()}")
    implementation("io.github.cdsap.talaiot:io.github.cdsap.talaiot.gradle.plugin:${libs.versions.talaiot.get()}")
    implementation("com.google.code.gson:gson:${libs.versions.gson.get()}")
}
