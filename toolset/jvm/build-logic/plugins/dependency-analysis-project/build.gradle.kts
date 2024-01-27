

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:platform"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin")
}
