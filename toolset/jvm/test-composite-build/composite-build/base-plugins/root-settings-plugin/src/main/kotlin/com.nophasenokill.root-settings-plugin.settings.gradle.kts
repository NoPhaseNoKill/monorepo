
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention")
}

pluginManagement {
    repositories {
        /*
            This allows us to load plugins in any of the subprojects during
            the top level plugin{} block. For example, we can load the producerPlugin
            directly into the consumerPlugin.
         */
        maven {
            url = uri("${rootProject.projectDir}/local-repo")
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        /*
            This allows us to load the dependencies of a plugin in any of the subprojects during
            the dependencies{} block. For example, we can load the producerPlugin
            directly into the consumerPlugin via:

            dependencies {
                implementation("com.nophasenokill:producer-plugin:1.0.0-local-dev")
            }
         */
        maven {
            url = uri("${rootProject.projectDir}/local-repo")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}