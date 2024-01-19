plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:platform"))

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")

//    implementation("com.nophasenokill.dependency-analysis:dependency-analysis-platform")
}