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
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.20") // Gives us access to kotlin compile interface in this file
        }
    }
}
include(":my-greeting-lib")
project(":my-greeting-lib").projectDir = file(File("modules/libraries/my-greeting-lib"))

include(":greeting-plugin")
project(":greeting-plugin").projectDir = file(File("modules/plugins/greeting-plugin"))

// gradle.lifecycle.beforeProject {
//
//     // Dynamically register tasks to publish plugins and compile libraries
//     val publishPlugins = tasks.register("buildPluginJars") {
//         // Include all projects that apply `java-gradle-plugin`
//         subprojects.filter { it.plugins.hasPlugin("java-gradle-plugin") }.forEach { pluginProject ->
//             dependsOn(pluginProject.tasks.named("jar"))
//             finalizedBy(pluginProject.tasks.named("publish"))
//         }
//     }
//
//     val buildAllSubprojects = tasks.register("buildAllSubprojects") {
//         // Include all library projects for compilation tasks
//         subprojects.filterNot { it.plugins.hasPlugin("java-gradle-plugin") }.forEach { libProject ->
//             dependsOn(libProject.tasks.withType<JavaCompile>())
//             dependsOn(libProject.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>())
//         }
//         mustRunAfter(publishPlugins)
//     }
//
//     // Register a top-level task to chain both plugin publishing and library building
//     tasks.register("buildAll") {
//         dependsOn(publishPlugins)
//         finalizedBy(buildAllSubprojects)
//     }
//
//     repositories {
//         gradlePluginPortal()
//     }
// }


