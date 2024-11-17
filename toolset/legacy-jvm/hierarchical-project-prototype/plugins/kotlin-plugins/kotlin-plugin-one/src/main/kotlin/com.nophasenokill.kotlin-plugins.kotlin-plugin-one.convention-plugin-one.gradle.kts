plugins {
    kotlin("jvm")
}

group = "com.nophasenokill.${name}"
version = "1.0-local"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)

    compilerOptions {
        progressiveMode.set(true)
    }
}

/*
    Allows us to use

        implementation("com.nophasenokill.kotlin-plugins:kotlin-plugin-one")
            OR
        implementation("com.nophasenokill.meta-plugins:meta-plugin-one")

        and have them resolve to the local projects

        :kotlin-plugin-one
            and
        :meta-plugin-one

        respectively
 */

configurations.all {
    val configuration = this.name

    resolutionStrategy.dependencySubstitution.all {
        println("[${project.name}] ${this.requested}")
        requested.let {
            when(it) {
                is DependencySubstitution -> {
                    if(it.displayName.contains("com.nophasenokill")) {
                        println("[${project.name}] Is DependencySubstitution: configuration: ${configuration} ${it}")
                    }

                }
                is ModuleComponentSelector -> {
                    if(it.displayName.contains("com.nophasenokill")) {
                        println("[${project.name}] Is ModuleComponentSelector: configuration: ${configuration} ${it}")
                    }
                }

                is ProjectComponentSelector -> {
                    if(it.displayName.contains("com.nophasenokill")) {
                        println("[${project.name}] Is ProjectComponentSelector: configuration: ${configuration} ${it}")
                    }
                }

                is LibraryComponentSelector -> {
                    if(it.displayName.contains("com.nophasenokill")) {
                        println("[${project.name}] Is LibraryComponentSelector: configuration: ${configuration} ${it}")
                    }
                }
                else -> {
                    println("Unknown selector: ${this}")
                }
            }
        }
    }
}

// configurations.all {
//     resolutionStrategy.dependencySubstitution {
//         substitute(module("com.nophasenokill.kotlin-plugins:kotlin-plugin-one")).using(project(":kotlin-plugin-one")).because("we work with the unreleased development version")
//     }
// }

// configurations.all {
//
//     resolutionStrategy.dependencySubstitution.all {
//         val requested = this.requested
//         if (requested is ModuleComponentSelector && requested.group.contains("com.nophasenokill")) {
//             println("""
//                 Requested group contains com.nophasenokill
//                     Info:
//                         name: ${requested.module}
//                         group: ${requested.group}
//                         version: ${requested.version}
//                         toString(): ${requested}
//             """.trimIndent())
//
//             // Substitute the requested dependency with the project from the included build
//             resolutionStrategy.dependencySubstitution.substitute(requested).using(this as ModuleComponentSelector)
//         }
//     }
// }

// configurations.all {
//
//     fun DependencySubstitutions.callback(dependency: DependencySubstitution) {
//         if(this is ModuleComponentSelector && (this as ModuleComponentSelector).group.contains("com.nophasenokill")) {
//             println("""
//                     Requested group contains com.nophasenokill
//                         Info:
//                             name: ${(this as ModuleComponentSelector).module}
//                             group: ${(this as ModuleComponentSelector).group}
//                             version: ${(this as ModuleComponentSelector).version}
//                             toString(): ${this}
//                 """.trimIndent())
//             substitute(dependency.requested).using(project(":${(this as ModuleComponentSelector).module}"))
//     }
//
//
//     resolutionStrategy.dependencySubstitution.all(::callback)
// }

// configurations.create("publishedRuntimeClasspath") {
//     resolutionStrategy.useGlobalDependencySubstitutionRules = false
//
//     extendsFrom(configurations.runtimeClasspath.get())
//     isCanBeConsumed = false
//     attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
// }

// repositories {
//     flatDir {
//         dirs("/tmp/repo/com/nophasenokill/kotlin-plugin-one/kotlin-plugin-one/0.1.local-dev")
//     }
// }

// Is ProjectComponentSelector: com.nophasenokill.kotlin-plugin-one:kotlin-plugin-one:0.1.local-dev
// Is ProjectComponentSelector: com.nophasenokill.kotlin-plugin-one:kotlin-plugin-one:0.1.local-dev
// Is ProjectComponentSelector: com.nophasenokill.kotlin-plugin-one:kotlin-plugin-one:0.1.local-dev
