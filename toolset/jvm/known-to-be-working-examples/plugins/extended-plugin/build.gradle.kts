import com.nophasenokill.DirHashTask

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    id("com.nophasenokill.hash-source-plugin")
    id("com.nophasenokill.build-service-warning-fix-plugin")
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
    implementation("org.gradle.kotlin:gradle-kotlin-dsl-plugins:${libs.versions.kotlinDsl.get()}") // Allows the plugin to apply `kotlin-dsl` inside of a test

    singleFileConfiguration(project(path = ":basic-plugin", configuration = "sharedConfiguration"))
    multiFileConfiguration("com.nophasenokill.hash-source-plugin:hash-source-plugin:0.1.local-dev")

    /*
        Example of the actual methods that get called under the covers
     */
    project.dependencies.add("testImplementation", project.dependencies.platform("org.junit:junit-bom:${libs.versions.junit.get()}"))
    project.dependencies.add("testImplementation", "org.junit.jupiter:junit-jupiter")
    project.dependencies.add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")

    /*
        Note: The below may not be required, due to the java-gradle-plugin pulling this in for automatically.

            project.dependencies.add("testImplementation", gradleTestKit()) - don't need

        However, if you require this on a different configuration - ie a custom one, you will still want the same
        behaviour. Custom configurations may include things like a functionalTest one - where your source sets are
        different. The easiest way to do this is via the gradle plugin, which also applies the kotlin bom so you can
        further aligned versions for the custom config:

            gradlePlugin {
                testSourceSets(functionalTestConfiguration)
            }

        I'm not 100% sure, but this is most likely the equivalent:

            project.dependencies.add("functionalTestConfiguration", project.dependencies.platform("org.jetbrains.kotlin:kotlin-bom")) - will need if you want to control transitives that testKit may pull in
            project.dependencies.add("functionalTestConfiguration", gradleTestKit()) - will need

            testSourceSets(functionalTestConfiguration)
     */
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

tasks.test {
    useJUnitPlatform()
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