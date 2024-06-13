
rootProject.name = "test-composite-build"

pluginManagement {
    repositories {
        includeBuild("composite-build/base-plugins/root-settings-plugin")
        gradlePluginPortal()

        /*
            This allows us to load plugins in any of the sub-projects during
            the top level plugin{} block. For example, we can load the producerPlugin
            directly into the consumerPlugin.
         */
        maven {
            url = uri("${rootProject.projectDir}/local-repo")
        }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":producer-plugin")
project(":producer-plugin").projectDir = file("composite-build/meta-plugins/producer-plugin")


include(":consumer-plugin")
project(":consumer-plugin").projectDir = file("composite-build/standard-plugins/consumer-plugin")


plugins {
    id("com.nophasenokill.root-settings-plugin")
}

