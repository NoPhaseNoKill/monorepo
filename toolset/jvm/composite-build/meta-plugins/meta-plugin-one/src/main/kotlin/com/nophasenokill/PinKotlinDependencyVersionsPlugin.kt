package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.VersionCatalogsExtension
import kotlin.apply as kotlinApply

class PinKotlinDependencyVersionsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        doIt(target)
    }

    // Need a separate fn since (kotlin)Apply returns a value, changing the method signature and breaking override
    private fun doIt(target: Project) = target.kotlinApply {
        pluginManager.apply("org.jetbrains.kotlin.jvm")

        // is `withPlugin` necessary given you applied that plugin on the previous line?
        // The doco would indicate that execution of this block is NOT delayed since the plugin
        // has already been applied ...
        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            // Using a 'provider':
            //   1. avoids all the explicit not null checks (has impact on WHEN error is reported if something IS null)
            //   2. links the provider source (and the error IF null) with usage, adding context (addresses #1?)
            //   3. simplifies use of value as Gradle provides API for adding dependencies with a provider
            val coroutinesVersion = provider {
                extensions
                    .getByType(VersionCatalogsExtension::class.java)
                    .find("libs")
                    .map { it.findVersion("coroutines") }
            }


            // Nice idea but I think this will cause more problems than solve because:
            //   1. it might not be accessible (think Corporate firewall ...) stopping the build
            //   2. repositories are applied in the order they are registered, which could give inconsistent
            //      behaviour if this plugin is added before/after another plugin that also registers a repository
            //      and/or a repository is added directly in a build/settings script.
            repositories.mavenCentral()
            /*
                Used to pin specific dependencies for transitive dependencies pulled in due
                to kotlin plugin.

                Current versions this is applying to is:
                    - org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm 1.5.0 -> 1.8.0
                    - org.jetbrains.kotlin:kotlin-reflect 1.6.0 -> 1.9.21

                Remaining dependency we can't touch is: kotlinCompilerClasspath which appears to have a direct
                need for reflect 1.6.10

                    kotlinCompilerClasspath
                    \--- org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.23
                     +--- org.jetbrains.kotlin:kotlin-stdlib:1.9.23
                     |    \--- org.jetbrains:annotations:13.0
                     +--- org.jetbrains.kotlin:kotlin-script-runtime:1.9.23
                     +--- org.jetbrains.kotlin:kotlin-reflect:1.6.10
             */

            fun DependencySet.addDependency(dependencyNotation: String) {
                add(dependencies.create(dependencyNotation))
            }

            fun DependencySet.addPlatform(dependencyNotation: String) {
                add(dependencies.platform(dependencyNotation))
            }

            configurations.findByName("kotlinKlibCommonizerClasspath")?.withDependencies {
                it.addPlatform("org.jetbrains.kotlin:kotlin-bom")
                it.addDependency("org.jetbrains.kotlin:kotlin-klib-commonizer-embeddable")
            }

            configurations.findByName("kotlinBuildToolsApiClasspath")?.withDependencies {
                it.addLater(coroutinesVersion.map { version ->
                    dependencies.platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:$version")
                })
                it.addPlatform("org.jetbrains.kotlin:kotlin-bom")
                it.addDependency("org.jetbrains.kotlin:kotlin-build-tools-impl")
            }

            configurations.findByName("implementation")?.withDependencies {
                it.addPlatform("com.nophasenokill.platforms:generalised-platform")
            }

            configurations.findByName("runtimeOnly")?.withDependencies {
                it.addDependency("org.slf4j:slf4j-simple")
            }
        }
    }
}

