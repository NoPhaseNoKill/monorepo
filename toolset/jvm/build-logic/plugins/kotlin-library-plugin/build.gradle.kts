

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:platform"))
    implementation(project(":commons-plugin"))

    implementation("com.autonomousapps:dependency-analysis-gradle-plugin")
    implementation(project(":dependency-analysis-project"))
}
