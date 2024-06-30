plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.nophasenokill.basic-kotlin-plugin"

gradlePlugin {
    val pluginBuildPlugin by plugins.creating {
        id = "com.nophasenokill.basic-kotlin-plugin"
        implementationClass = "com.nophasenokill.PluginBuildPlugin"
    }
}

dependencies {
    implementation("org.gradle.kotlin:gradle-kotlin-dsl-plugins:4.3.0")

    implementation(gradleApi())
    testImplementation(gradleTestKit())

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}