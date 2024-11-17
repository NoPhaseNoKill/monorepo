//
//
// rootProject.name = "jvm"
//
// /*
//     Ensures consistent versions across included builds and subprojects
//     This includes the root build/root build.gradle.kts file
//
//     PluginManagement Scope:
//     The pluginManagement block only applies to the current build.
//      When using includeBuild(), the included builds are treated as separate,
//      independent builds, and they do not inherit pluginManagement settings from the root project.
//
//      This means, to centralize version management of kotlin plugins for instance, we have two options:
//         1. Use the same plugin management configuration for each separate included build or;
//         2. use buildSrc to centralize the versions
//
//         This approach utilizes the best aspects of both, whereby we:
//
//         1. Use convention plugins to re-use the same plugin management configuration across builds
//         2. Use buildSrc (rather than version catalogs or any other means) for the centralized versions
//         3. Apply the 'static' versions to the convention plugin for each project
//
//         While it's tempting to use the versions.toml/other means to centralize versions, there are
//         significant downsides of each approach - and after playing with these - this feels the most
//         risk-free (less chance of developers misconfiguring dependencies which have subtle flow on
//         effects), means that timing issues for loading the version catalog aren't an issue,
//         all while achieving the same outcome.
//
//         The other added benefit is that you can structure these however you want, and are not limited to gradle's dsl
//         which implicitly uses .dot notation for each 'part' of a dependency/plugin declaration. It also means the buildSrc
//         itself can call the plugin to configure itself, whilst maintaining clear separation of where you may want to
//         structure your code base.
//
//         An example is shown below of how this is intended to be wired up:
//
//         // buildSrc/src/main/kotlin/Versions.kt
//             object Versions {
//                 const val KOTLIN = "2.0.20"
//             }
//
//         // buildSrc/build.gradle.kts
//
//             // Note: all includedBuilds are implicitly pulled in for buildSrc - so we do not need to specify the includedBuild
//             // in pluginManagement like we do for other projects
//             plugins {
//                 id("com.nophasenokill.root-plugins.basic-settings-plugin")
//             }
//
//         // some-nested-folder/some-nested-project.settings.gradle.kts
//
//             pluginManagement {
//                 includeBuild("build-logic/settings-plugins")
//             }
//
//             plugins {
//                 id("com.nophasenokill.root-plugins.basic-settings-plugin")
//             }
//
//         // build-logic/root-plugins/basic-settings-plugin/com.nophasenokill.root-plugins.basic-settings-plugin.settings.gradle.kts
//
//             pluginManagement {
//                 repositories {
//                     gradlePluginPortal()
//                 }
//                 plugins {
//                     id("org.jetbrains.kotlin.jvm") version Versions.KOTLIN
//                 }
//             }
//  */
// pluginManagement {
//     includeBuild("creme-de-la-creme-project-layout/build-logic")
//     repositories {
//         gradlePluginPortal()
//     }
// }
//
// plugins {
//     id("root-settings")
// }
//
// includeBuild("creme-de-la-creme-project-layout/some-other-build")
//
// includeProject("some-subproject-that-depends-on-local-plugin", ProjectType.CREME_DE_LA_CREME_PROJECT_LAYOUT)
// includeProject("some-subproject-with-no-dependencies-on-local-plugin", ProjectType.CREME_DE_LA_CREME_PROJECT_LAYOUT)
//
// // include(":some-subproject-that-depends-on-local-plugin")
// // project(":some-subproject-with-no-dependencies-on-local-plugin").projectDir = file(File("creme-de-la-creme-project-layout/some-subproject-that-depends-on-local-plugin"))
// //
// // include(":some-subproject-with-no-dependencies-on-local-plugin")
// // project(":some-subproject-with-no-dependencies-on-local-plugin").projectDir = file(File("creme-de-la-creme-project-layout/some-subproject-with-no-dependencies-on-local-plugin"))
//
// enum class ProjectType(val path: String) {
//     CREME_DE_LA_CREME_PROJECT_LAYOUT("creme-de-la-creme-project-layout"),
// }
//
// fun includeProject(projectName: String, type: ProjectType) {
//     val projectNamePrefix = type.path.replace("${File.separatorChar}", ":")
//     println(
//         """
//
//         Old name: ${projectName}
//         New name: ${projectNamePrefix}:$projectName
//         Project name prefix: ${projectNamePrefix}, type: ${type.path}, project name: ${projectName}
//
//     """.trimIndent()
//     )
//     include(":${projectNamePrefix}:$projectName")
//     project(":${projectNamePrefix}:$projectName").projectDir = file(File("${type.path}/${projectName}"))
// }
//
// enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
// enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
//
// // println("Hello from ${file(".")}")
//
// gradle.lifecycle.beforeProject {
//
//     val conf = configurations.create("conf")
//
//     conf.defaultDependencies {
//         println("TOMMY G")
//
//         // this.add(project.dependencies.create("org.jetbrains:annotations:13.0"))
//         // this.add(project.dependencies.create("org.jetbrains.kotlin:kotlin-compiler-embeddable:${project.getKotlinPluginVersion()}"))
//         // this.add(project.dependencies.create("org.jetbrains:annotations:23.0.0"))
//         this.add(project.dependencies.create("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.20"))
//         // this.add(project.dependencies.create("org.jetbrains.kotlin:kotlin-stdlib:${project.getKotlinPluginVersion()}"))
//         // this.add(project.dependencies.create("org.jetbrains.kotlin:kotlin-script-runtime:${project.getKotlinPluginVersion()}"))
//         // this.add(project.dependencies.create("org.jetbrains.kotlin:kotlin-reflect:${project.getKotlinPluginVersion()}"))
//         // this.add(project.dependencies.create("org.jetbrains.kotlin:kotlin-daemon-embeddable:${project.getKotlinPluginVersion()}"))
//         // this.add(project.dependencies.create("org.jetbrains.intellij.deps:trove4j:1.0.20200330"))
//         // this.add(project.dependencies.create("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.1"))
//
//         // +--- org.jetbrains:annotations:13.0
//         // \--- org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.20
//         // +--- org.jetbrains.kotlin:kotlin-stdlib:2.0.20
//         // +--- org.jetbrains.kotlin:kotlin-script-runtime:2.0.20
//         // +--- org.jetbrains.kotlin:kotlin-reflect:1.6.10
//         // +--- org.jetbrains.kotlin:kotlin-daemon-embeddable:2.0.20
//         // +--- org.jetbrains.intellij.deps:trove4j:1.0.20200330
//         // \--- org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4
//     }
// }
