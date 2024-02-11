
plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(project(":base-plugin"))
    implementation(platform("com.nophasenokill.platform:platform"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
}