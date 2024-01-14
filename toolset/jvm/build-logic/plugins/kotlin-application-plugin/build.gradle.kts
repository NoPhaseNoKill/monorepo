plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platforms:platforms"))

    implementation(project(":commons-plugin"))
}
