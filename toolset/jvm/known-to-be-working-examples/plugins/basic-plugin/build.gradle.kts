plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    id("com.nophasenokill.build-service-warning-fix-plugin")
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
    implementation("org.gradle.kotlin:gradle-kotlin-dsl-plugins:${libs.versions.kotlinDsl.get()}")
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

publishing {
    repositories {
        maven {
            setUrl(file("../../../local-repo"))
        }
    }
    publications {
        create<MavenPublication>("maven") { from(components["java"]) }
    }
}
