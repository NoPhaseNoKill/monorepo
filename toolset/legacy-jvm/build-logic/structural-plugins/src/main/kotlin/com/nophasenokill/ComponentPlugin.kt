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

            gradle.beforeSettings {
                pluginManagement.repositories {
                    google()
                    mavenCentral()
                    gradlePluginPortal()
                }

                dependencyResolutionManagement {
                    repositories {
                        mavenCentral()
                        gradlePluginPortal()
                    }
                }
            }

            pluginManager.apply("org.gradle.toolchains.foojay-resolver-convention")
            pluginManager.apply(DevelocityPlugin::class.java)

            pluginManager.withPlugin("com.gradle.develocity") {
                develocity {
                    buildScan {
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

                        obfuscation {
                            username { OBFUSCATED_USERNAME }
                            ipAddresses { addresses -> addresses.map { _ -> OBFUSCATED_HOSTNAME } }
                            hostname { OBFUSCATED_IP_ADDRESS }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val OBFUSCATED_USERNAME = "OBFUSCATED_USERNAME"
        const val OBFUSCATED_HOSTNAME = "OBFUSCATED_HOSTNAME"
        const val OBFUSCATED_IP_ADDRESS = "OBFUSCATED_IP_ADDRESS"
    }
}
