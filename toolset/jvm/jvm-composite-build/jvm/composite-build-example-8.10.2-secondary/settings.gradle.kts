rootProject.name = "consumer"

pluginManagement {
    buildscript {
        repositories {
            maven {
                url =
                    uri(rootDir.resolve("modules/plugins/greeting-plugin/build/repo"))
            }
            gradlePluginPortal()
        }

        dependencies {
            classpath("org.sample.greeting:org.sample.greeting.gradle.plugin:1.0-SNAPSHOT")
        }
    }
}
include(":modules:libraries:my-greeting-lib")
include(":modules:plugins:greeting-plugin")

gradle.lifecycle.beforeProject {
    repositories {
        gradlePluginPortal()
    }
}

