plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))

    implementation(project(":base-plugin"))
    implementation(project(":junit-test-plugin"))

}