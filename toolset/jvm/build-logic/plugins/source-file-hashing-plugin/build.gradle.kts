

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("commons-io:commons-io:${libs.versions.commonsIo.get()}")
    implementation("commons-codec:commons-codec:${libs.versions.commonsCodec.get()}")
}


