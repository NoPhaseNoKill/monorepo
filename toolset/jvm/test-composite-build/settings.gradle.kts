
rootProject.name = "test-composite-build"

pluginManagement {
    repositories {
     /*
         We need the both of these to be able to load the initial root settings
         from a another build/binary.
      */
        maven {
            url = uri("${rootProject.projectDir}/local-repo")
        }
        gradlePluginPortal()
    }
}
/*
    Do NOT declare dependency management here - it is being
    managed inside of the root-settings-plugin
 */


plugins {
    id("com.nophasenokill.root-settings-plugin") version("1.0.0-local-dev")
}

include("composite-build:base-plugins:root-settings-plugin")
include("composite-build:base-build-plugins:standalone-kotlin-base-plugin")
include("composite-build:meta-plugins:producer-plugin")
include("composite-build:standard-plugins:consumer-plugin")