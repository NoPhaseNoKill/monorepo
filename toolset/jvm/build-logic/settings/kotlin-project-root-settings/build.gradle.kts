plugins {
    `kotlin-dsl`
}



dependencies {
   platform(":platforms:plugins-platform")
    // println("${project("com.nophasenokill.platform.platforms:plugins-platform")}")
    // println(implementation("com.nophasenokill.platform:platforms:plugins-platform"))

    // implementation(platform("com.nophasenokill.platform:plugins-platform"))

    implementation("org.gradle.toolchains:foojay-resolver:0.7.0")
    implementation("com.gradle:gradle-enterprise-gradle-plugin:3.15.1")
}

