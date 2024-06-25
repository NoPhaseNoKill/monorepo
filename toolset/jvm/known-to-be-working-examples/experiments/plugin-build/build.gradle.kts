plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.nophasenokill.basic-plugin"

gradlePlugin {
    val pluginBuildPlugin by plugins.creating {
        id = "com.nophasenokill.basic-plugin"
        implementationClass = "com.nophasenokill.PluginBuildPlugin"
    }
}

dependencies {
    implementation("org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:4.3.1")

    implementation(gradleApi())
    testImplementation(gradleTestKit())

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}