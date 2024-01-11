plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.nophasenokill.platform:base-platform"))
    implementation("org.gradle.toolchains:foojay-resolver")
    implementation("com.gradle.enterprise:com.gradle.enterprise.gradle.plugin")
}