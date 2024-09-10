plugins {
    alias(libs.plugins.kotlinDsl)
}

gradlePlugin {
    plugins {
        create("componentPlugin") {
            id = "com.nophasenokill.component-plugin"
            implementationClass = "com.nophasenokill.ComponentPlugin"
        }
    }
}


dependencies {
    implementation("com.gradle:develocity-gradle-plugin:${libs.versions.gradleEnterprise.get()}")
    implementation("org.gradle.toolchains:foojay-resolver:${libs.versions.fooJayResolver.get()}")
}
