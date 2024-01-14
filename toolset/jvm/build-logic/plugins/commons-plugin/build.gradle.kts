plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platforms:platforms"))

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation(project(":dependency-analysis-plugins"))
}