

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation(project(":base-plugin"))

    implementation("commons-io:commons-io")
    implementation("commons-codec:commons-codec")
}


