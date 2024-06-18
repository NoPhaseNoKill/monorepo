
rootProject.name = "test-composite-build"

includeBuild("composite-build/standard-plugins")

include("composite-build:libraries:consumer-kotlin-plugin")
includeBuild("composite-build/standalone-plugin")
includeBuild("composite-build/meta-plugin")

/*
    Binary plugins published as external jar files can be added
    to a project by adding the plugin to the build script classpath
    and then applying the plugin.

    External jars can be added to the build script classpath
    using the buildscript{} block as described in External
    dependencies for the build script:

    build.gradle.kts

    buildscript {
        repositories {
            gradlePluginPortal()
        }
        dependencies {
            classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
        }
    }

    apply(plugin = "com.jfrog.bintray")


 */


/*
        plugins {
            id(«plugin id») <------- for core Gradle plugins or plugins already available to the build script
            id(«plugin id») version «plugin version» <------for binary Gradle plugins that need to be resolved
        }
 */



/*
    Apply a core, community or local plugin needed for the build script itself.
 */
// buildscript {
//     repositories {
//         maven {
//             url = uri("https://plugins.gradle.org/m2/")
//         }
//     }
//     dependencies {
//         classpath("org.barfuin.gradle.taskinfo:gradle-taskinfo:2.1.0")
//     }
// }
// plugins {
//     id("org.barfuin.gradle.taskinfo") version "2.1.0"
// }
//
//
//
// pluginManagement {
//     val helloPluginVersion: String by settings
//     plugins {
//         id("com.example.hello") version helloPluginVersion
//     }
// }



