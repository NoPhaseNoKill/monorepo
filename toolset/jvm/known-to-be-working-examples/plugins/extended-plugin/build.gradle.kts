import com.nophasenokill.DirHashTask

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.nophasenokill.hash-source-plugin")
}

group = "com.nophasenokill.extended-plugin"
version = "0.1.local-dev"


gradlePlugin {
    val pluginBuildPlugin by plugins.creating {
        id = "com.nophasenokill.extended-plugin"
        implementationClass = "com.nophasenokill.ExtendedPlugin"
    }
}

val singleFileConfiguration: Configuration by configurations.creating {
    isCanBeConsumed = false
}

/*
    This is the equivalent of doing:
        dependencies {
            implementation("com.nophasenokill.hash-source-plugin:hash-source-plugin:0.1.local-dev")
        }

        ie this is equal to:

        dependencies {
            multiFileConfiguration("com.nophasenokill.hash-source-plugin:hash-source-plugin:0.1.local-dev")
        }

    But we now have our own specific configuration which could possibly be shared if we wanted it to be
 */
val multiFileConfiguration: Configuration by configurations.creating {
    isCanBeConsumed = false

    val implementation = configurations.implementation.get()
    extendsFrom(implementation)
    implementation.dependencies.add(dependencyFactory.create("com.nophasenokill.hash-source-plugin:hash-source-plugin:0.1.local-dev"))
}

dependencies {
    singleFileConfiguration(project(path = ":known-to-be-working-examples:plugins:basic-plugin", configuration = "sharedConfiguration"))
    multiFileConfiguration("com.nophasenokill.hash-source-plugin:hash-source-plugin:0.1.local-dev")
}

tasks.register("showFile") {
    val sharedFiles: FileCollection = singleFileConfiguration
    inputs.files(sharedFiles)
    doFirst {
        logger.lifecycle("Shared file contains the text: '{}'. \n File located at: '{}'", sharedFiles.singleFile.readText(), sharedFiles.singleFile.path)
    }
}

/*
    Showcases how we can ensure we know about any transitives
    being updated in any way and get a reproducible hash of all
    configurations, which include any files linked to a shared
    configuration.
 */

tasks.register<DirHashTask>("getHashForAllDependencies") {

    val configurations = configurations.filter{ it.isCanBeResolved }.map {
        logger.quiet("Configuration being included for getHashForAllDependencies is: ${it.name}")
        it
    }
    inputs.files(configurations)

    contents.from(multiFileConfiguration, singleFileConfiguration)
    hashMethod.set("MD5")
    outputDir.set(project.layout.buildDirectory.dir("hash-of-all-dependencies"))
}