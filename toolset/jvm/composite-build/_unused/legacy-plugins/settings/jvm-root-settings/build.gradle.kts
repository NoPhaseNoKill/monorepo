plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.gradle:common-custom-user-data-gradle-plugin:${libs.versions.commonCustomUserDataGradlePlugin.get()}")
    implementation("com.gradle:gradle-enterprise-gradle-plugin:${libs.versions.gradleEnterpriseGradlePlugin.get()}")
    implementation("org.gradle.toolchains:foojay-resolver:${libs.versions.foojayResolver.get()}")
}