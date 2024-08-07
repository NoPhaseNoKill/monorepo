package com.nophasenokill

import com.gradle.develocity.agent.gradle.DevelocityPlugin
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.caching.http.HttpBuildCache
import org.gradle.kotlin.dsl.develocity
import java.io.File
import java.net.URI

class ComponentPlugin: Plugin<Settings> {
    override fun apply(settings: Settings) {

        settings.run {

            settings.pluginManagement.repositories.gradlePluginPortal()
            settings.dependencyResolutionManagement.repositories.mavenCentral()

            settings.plugins.apply(DevelocityPlugin::class.java)

            println("Build cache hashcode from component plugin is: ${buildCache.hashCode()}")
            println("Settings Build cache hashcode from component plugin is: ${settings.buildCache.hashCode()}")


            settings.plugins.withType(DevelocityPlugin::class.java) {
                settings.extensions.findByType(DevelocityPlugin::class.java).run {

                        develocity.run {
                            buildScan.run {
                                uploadInBackground.set(false)
                                termsOfUseUrl.set("https://gradle.com/terms-of-service")
                                termsOfUseAgree.set("yes")

                                /*
                                    If you try to abstract these strings to a 'val' outside gradleEnterprise lambda, you get errors
                                    such as below:

                                    4 problems were found storing the configuration cache.
                                    - Gradle runtime: cannot serialize Gradle script object references as these are not supported with the configuration cache.
                                      See https://docs.gradle.org/8.5/userguide/configuration_cache.html#config_cache:requirements:disallowed_types
                                        ...
                                        ...
                                 */
                                obfuscation.run {
                                    username { "OBFUSCATED_USERNAME" }
                                    ipAddresses { addresses -> addresses.map { _ -> "OBFUSCATED_IP_ADDRESS" } }
                                    hostname { "OBFUSCATED_HOSTNAME" }
                                }
                            }
                        }

                        buildCache.run {
                            local.run {
                                isEnabled = true
                            }

                            remote(HttpBuildCache::class.java).run {
                                isEnabled = true
                                isPush = true
                                isAllowUntrustedServer = true
                                isAllowInsecureProtocol = true
                                url = URI("http://localhost:5071/cache/")
                                credentials.run {
                                    username = "tom"
                                    password = "Password01!" // used for showcase
                                }
                            }
                        }
                }
            }

            fun includeBuildsAndProjects(dir: File) {
                dir.walk().forEach { file ->
                    if (file.isFile && file.name == "build.gradle.kts") {
                        // Include any projects
                        val relativePath = file.parentFile.relativeTo(rootDir).path.replace(File.separator, ":")
                        include(relativePath)
                        project(":$relativePath").projectDir = file.parentFile
                    } else if (file.isFile && file.name == "settings.gradle.kts") {
                        // Include any builds
                        includeBuild(file.parentFile)
                    }
                }
            }

            includeBuildsAndProjects(rootDir)
            enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
            enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
        }
    }
}