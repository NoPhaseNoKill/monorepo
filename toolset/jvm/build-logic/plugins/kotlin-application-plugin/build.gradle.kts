plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:plugins-platform"))
    testImplementation(platform("com.nophasenokill.platform:test-platform"))

    implementation(project(":commons-plugin"))
}
