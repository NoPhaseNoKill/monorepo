plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.nophasenokill.basic-plugin"
version = "0.1.local-dev"

gradlePlugin {
    val pluginBuildPlugin by plugins.creating {
        id = "com.nophasenokill.basic-plugin"
        implementationClass = "com.nophasenokill.BasicPlugin"
    }
}

dependencies {
    implementation("org.gradle.kotlin.kotlin-dsl:org.gradle.kotlin.kotlin-dsl.gradle.plugin:4.3.1")
    implementation(gradleApi())
    testImplementation(gradleTestKit())
}

val makeFile = tasks.register("makeFile") {
    val sharedFile = layout.buildDirectory.file("some-subdir/shared-file.txt")
    outputs.file(sharedFile)
    doFirst {
        sharedFile.get().asFile.writeText("This file is shared across Gradle subprojects.")
    }
}

val sharedConfiguration by configurations.creating {
    isCanBeResolved = false
}

artifacts {
    add(sharedConfiguration.name, makeFile)
}