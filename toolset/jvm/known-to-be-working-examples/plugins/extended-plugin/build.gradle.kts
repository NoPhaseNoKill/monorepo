plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.nophasenokill.extended-plugin"
version = "0.1.local-dev"



gradlePlugin {
    val pluginBuildPlugin by plugins.creating {
        id = "com.nophasenokill.extended-plugin"
        implementationClass = "com.nophasenokill.ExtendedPlugin"
    }
}

val sharedConfiguration: Configuration by configurations.creating {
    isCanBeConsumed = false
}

dependencies {
    sharedConfiguration(project(path = ":known-to-be-working-examples:plugins:basic-plugin", configuration = "sharedConfiguration"))
}

tasks.register("showFile") {
    val sharedFiles: FileCollection = sharedConfiguration
    inputs.files(sharedFiles)
    doFirst {
        logger.lifecycle("Shared file contains the text: '{}'", sharedFiles.singleFile.readText())
    }
}

