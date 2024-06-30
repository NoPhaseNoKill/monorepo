package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension

class PinKotlinDependencyVersionsPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        target.pluginManager.apply("org.jetbrains.kotlin.jvm")

        target.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            val catalogsExtension = requireNotNull(target.extensions.findByType(VersionCatalogsExtension::class.java))

            val libs = requireNotNull(catalogsExtension.find("libs").get())
            val coroutinesVersion = requireNotNull(libs.findVersion("coroutines").get())

            target.repositories.mavenCentral()
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

            if(target.configurations.findByName("kotlinKlibCommonizerClasspath") != null) {
                val buildToolConfig = target.configurations.findByName("kotlinKlibCommonizerClasspath")

                buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinKlibCommonizerClasspath", target.dependencies.platform("org.jetbrains.kotlin:kotlin-bom")))
                buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinKlibCommonizerClasspath", "org.jetbrains.kotlin:kotlin-klib-commonizer-embeddable"))

            }

            if(target.configurations.findByName("kotlinBuildToolsApiClasspath") != null) {
                val buildToolConfig = target.configurations.findByName("kotlinBuildToolsApiClasspath")

                buildToolConfig?.dependencies?.add(
                    target.dependencies.add("kotlinBuildToolsApiClasspath",
                        target.dependencies.platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:${coroutinesVersion}")
                    )
                )
                buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinBuildToolsApiClasspath", target.dependencies.platform("org.jetbrains.kotlin:kotlin-bom")))
                buildToolConfig?.dependencies?.add(target.dependencies.add("kotlinBuildToolsApiClasspath", "org.jetbrains.kotlin:kotlin-build-tools-impl"))
            }

            if(target.configurations.findByName("implementation") != null) {
                val implementation = target.configurations.findByName("implementation")
                implementation?.dependencies?.add(target.dependencies.add("implementation", target.dependencies.platform("com.nophasenokill.platforms:generalised-platform")))
            }

            if(target.configurations.findByName("runtimeOnly") != null) {
                val runtimeOnlyConfig = target.configurations.findByName("runtimeOnly")
                runtimeOnlyConfig?.dependencies?.add(target.dependencies.add("runtimeOnly", "org.slf4j:slf4j-simple"))
            }
        }
    }
}

