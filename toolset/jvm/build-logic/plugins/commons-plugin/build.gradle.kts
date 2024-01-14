plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:plugins-platform"))

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation(project(":dependency-analysis-plugins"))
}