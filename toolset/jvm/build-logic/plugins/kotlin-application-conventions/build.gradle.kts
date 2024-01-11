plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:base-platform"))
    implementation(project(":kotlin-base-conventions"))

}
