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

                            remote(HttpBuildCache::class.java   ).run {
                                isEnabled = false // turn on in future
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
            /*
                You need to be VERY careful when changing this.

                There are VERY subtle nuances, particularly where instrumentation is happening/the logs are non-obvious.

                The below code:
                    1. Walks the project, and includes any subprojects or builds which are inside of it
                    2. Excludes buildSrc and root
                    3. Flattens the project name to prevent gradle from causing a bug where nested project structure
                     gets included multiple times, even though the docs at:https://docs.gradle.org/8.9/userguide/multi_project_builds.html#sub:modifying_element_of_the_project_tree
                     say it should still have worked.

                Easiest way to confirm if you've broken something is by doing:

                Looking at the OLD broken expectations:


                    ├── subs // Gradle would see this as a subproject
                    │   └── web // Gradle would see this as a subproject
                    │       └── my-web-module // Intended subproject
                    │           ...
                    │           └── build.gradle


                Ensuring we don't get these in the new run when running './gradlew projects' command:


                    Root project 'jvm'
                    +--- Project ':standalone-projects-applications-application-one'
                    +--- Project ':standalone-projects-applications-application-two'
                    +--- Project ':standalone-projects-applications-application-with-instrumentation'
                    +--- Project ':standalone-projects-applications-build-tool-ui'
                    +--- Project ':standalone-projects-applications-example-desktop-application'
                    +--- Project ':standalone-projects-applications-example-package-name-relocation-app'
                    +--- Project ':standalone-projects-libraries-example-library-three'
                    +--- Project ':standalone-projects-libraries-ksp-processor'
                    +--- Project ':standalone-projects-libraries-library-one'
                    +--- Project ':standalone-projects-libraries-library-two'
                    \--- Project ':standalone-projects-libraries-some-new-lib'

                    Included builds:

                    +--- Included build ':build-logic'
                    \--- Included build ':build-logic-meta'
             */

            fun includeBuildsAndProjects(dir: File) {
                dir.walk().forEach { file ->
                    if (file.isFile && file.name == "build.gradle.kts" && !file.path.contains("buildSrc")) {
                        // Include any projects
                        val relativePath = file.parentFile.relativeTo(rootDir).path.replace(File.separator, ":")
                        val projectName = relativePath.replace(":", "-") // Convert to a flat project name
                        include(":$projectName")
                        project(":$projectName").projectDir = file.parentFile
                    } else if (file.isFile && file.name == "settings.gradle.kts" && !file.path.contains("buildSrc")) {
                        // Include any builds
                        includeBuild(file.parentFile)
                    }
                }
            }

            includeBuildsAndProjects(rootDir)
            settings.enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
            settings.enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
        }
    }
}