plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:plugins-platform"))

    implementation(project(":commons"))
}
