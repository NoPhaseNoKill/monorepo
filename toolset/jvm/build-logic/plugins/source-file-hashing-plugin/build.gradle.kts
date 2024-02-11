

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation(project(":commons-plugin"))

    implementation("commons-io:commons-io")
    implementation("commons-codec:commons-codec")
}


