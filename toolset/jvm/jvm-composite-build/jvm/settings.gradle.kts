//
//
//
// pluginManagement {
//
//     /*
//         TODO See if we can somehow move this downwards
//
//         Currently it appears as though this needs to be at the root level for some reason
//         and cannot be moved downwards.
//
//         Otherwise we get:
//
//             settings.gradle.kts
//             org.jetbrains.kotlin:kotlin-stdlib:{strictly 1.9.24} -> 1.9.24 constraint
//
//      */
//     // buildscript {
//     //     configurations.all {
//     //         resolutionStrategy {
//     //             force("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
//     //         }
//     //     }
//     // }
//
//     println("Hash code: ${settings.getBuildscript().hashCode()}")
//     println("Hash code: ${buildscript.hashCode()}")
//     // buildscript {
//     //     configurations.classpath {
//     //         resolutionStrategy {
//     //             eachDependency {
//     //                 if (this.requested.group == "org.jetbrains.kotlin") {
//     //                     this.useVersion("2.0.21")
//     //                 }
//     //             }
//     //         }
//     //     }
//     // }
//
//
//     // buildscript.configurations.all {
//     //     println("Forced modules for ${this.name}: ${this.resolutionStrategy.forcedModules}")
//     // }
//
//     includeBuild("modules/meta-plugins/root-settings-plugin")
// }
//
//
rootProject.name = "jvm"
//
//
// plugins {
//     /*
//         Must be published to $rootDir/local-repo before this can get resolved
//      */
//     id("com.nophasenokill.root-settings-plugin")
//
// }
//
//
//


// buildscript {
//     dependencies {
//         classpath("org.gradle.kotlin:gradle-kotlin-dsl-plugins:5.1.2")
//     }
// }

// buildscript {
//
//     repositories.gradlePluginPortal()
//
//     dependencies {
//         // classpath("org.gradle.kotlin:gradle-kotlin-dsl-plugins:5.1.2")
//         // classpath("com.gradle:develocity-gradle-plugin:3.18.1")
//     }
// }

// plugins {
//     base
// }

// settings.buildscript.configurations.all {
//     if (this.name == "classpath") {
//         this.allDependencyConstraints.find { it.name.contains("stdlib") }?.version {
//             this.strictly("2.0.21")
//         }
//         val constraints = this.allDependencyConstraints
//         constraints.forEach { constraint ->
//             constraint.version {
//                 strictly("2.0.21")
//             }
//         }
//     }
// }


gradle.lifecycle.beforeProject {
    buildscript {

        repositories {
            dependencyResolutionManagement {  }
        }
        dependencies.classpath("com.gradle:develocity-gradle-plugin:3.18.1")

        repositories {
            mavenCentral()
            gradlePluginPortal()

        }
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

// buildscript {
//
//     dependencies {
//         classpath("com.gradle:develocity-gradle-plugin:3.18.1")
//     }
//
//
// }


gradle.lifecycle.beforeProject {
    plugins.apply("java") // so that I can use "implementation", "testImplementation" configurations


    /*
        Equivalent of:

        buildscript {
            configurations.classpath {
                isTransitive = false
            }
        }

     */

    this.buildscript.configurations.all {
        isTransitive = false
    }

// plugins.apply("base")
    this.buildscript.repositories.gradlePluginPortal()
    this.buildscript.repositories.maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
    this.buildscript.repositories.mavenCentral()
    this.buildscript.dependencies.add("classpath", this.buildscript.dependencies.platform("org.jetbrains.kotlin:kotlin-gradle-plugins-bom:2.1.0-Beta1"))
    this.buildscript.dependencies.add("classpath", this.buildscript.dependencies.platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.9.0"))


    this.buildscript.dependencies.add("classpath", "org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")
    this.buildscript.dependencies.add("classpath", "com.gradle:develocity-gradle-plugin:3.18.1")
    this.buildscript.dependencies.add("classpath", "org.jetbrains.kotlin:kotlin-reflect:2.1.0-Beta1")
    this.buildscript.dependencies.add("classpath", "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0")




//     this.buildscript.dependencies.add("classpath", this.buildscript.dependencies.platform("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0"))
//     this.buildscript.dependencies.add("classpath", this.buildscript.dependencies.platform("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.9.0"))
//     this.buildscript.dependencies.add("classpath", this.buildscript.dependencies.platform("org.jetbrains.kotlin:kotlin-gradle-plugins-bom:2.0.21"))
//     this.buildscript.dependencies.add("classpath", this.buildscript.dependencies.platform("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.21"))
// //     this.buildscript.dependencies.add("classpath", "com.gradle:develocity-gradle-plugin:3.18.1")
//     this.buildscript.dependencies.add("classpath", this.buildscript.dependencies.platform("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.0.21"))
//     this.buildscript.dependencies.add("classpath", "org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.0.21")
//     // this.buildscript.dependencies.add("classpath","org.gradle.kotlin:gradle-kotlin-dsl-plugins:5.1.2")
}

// includeBuild("modules/meta-plugins/root-settings-plugin")

// plugins {
//     id("org.gradle.kotlin.embedded-kotlin") version("5.1.2")
// }

// buildscript {
//     dependencies.embeddedKotlin("2.0.21")
// }
//
// plugins {
//     `embedded-kotlin`
// }
//
// gradle.beforeProject {
//     pluginManager.apply("java")
// }

//
// settings.apply(plugin = "java")
//
// includeBuild(".")

