package com.nophasenokill

import com.gradle.develocity.agent.gradle.DevelocityPlugin
import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity

class ComponentPlugin: Plugin<Settings> {
    override fun apply(settings: Settings) {

        fun locationVersionsMatch(): Boolean {
            val areSame = System.getProperty("java.home") == System.getenv("JAVA_HOME")
            if (areSame) {
                return true
            }

            if (System.getProperty("java.home") !== null &&
                System.getProperty("java.home").contains(".sdkman") &&
                System.getenv("JAVA_HOME").endsWith("/current")
            ) {
                return true
            }

            return false
        }


        settings.run {
            val expectedVersion = JavaVersion.VERSION_21
            val isCurrent = JavaVersion.current() == expectedVersion
            val meetsRequirements = locationVersionsMatch() && isCurrent

            if (!meetsRequirements) {
                val message = """
                    There was a problem with your java config. This can lead to issues.
                    Please confirm:
                        - You have JDK ${expectedVersion} or higher. Current version is ${JavaVersion.current()}
                        - Your System.getProperty("java.home") is the same as your System.getenv("JAVA_HOME")
                          System.getProperty("java.home") is: ${System.getProperty("java.home")}
                          System.getenv("JAVA_HOME") is:  ${System.getenv("JAVA_HOME")}
                        These will eliminate strange side effects from IDE's or misconfigurations down the track
                """.trimIndent()
                throw GradleException(message)
            }

            settings.gradle.beforeSettings {
                pluginManagement.repositories {
                    google()
                    mavenCentral()
                    gradlePluginPortal()
                }

                dependencyResolutionManagement {
                    repositories {
                        mavenCentral()
                    }
                }
            }

            pluginManager.apply("org.gradle.toolchains.foojay-resolver-convention")

            plugins.apply(DevelocityPlugin::class.java)

            plugins.withType(DevelocityPlugin::class.java) {
                extensions.findByType(DevelocityPlugin::class.java).run {

                    develocity.run {
                        buildScan.run {
                            publishing.onlyIf {
                                val hasFailures = it.buildResult.failures.isNotEmpty()
                                println("Decision to publish build scan was: ${hasFailures}.")
                                if (hasFailures) {
                                    println("Failures were: ${it.buildResult.failures}")
                                }
                                hasFailures
                            }

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
                }
            }
        }
    }
}
