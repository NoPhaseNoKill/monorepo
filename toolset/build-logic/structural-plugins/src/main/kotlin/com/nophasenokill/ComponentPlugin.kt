package com.nophasenokill

import com.gradle.develocity.agent.gradle.DevelocityPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.caching.http.HttpBuildCache
import org.gradle.kotlin.dsl.develocity
import java.net.URI

class ComponentPlugin: Plugin<Settings> {
    override fun apply(settings: Settings) {


        settings.run {

//             fun getBuildJavaHome() = System.getProperty("java.home")
// // System.getenv("JAVA_HOME")
// // fun getLegacyBuildJavaHome() = System.getProperty("java.home")
//
//             gradle.settingsEvaluated {
//                 if ("true" == System.getProperty("org.gradle.ignoreBuildJavaVersionCheck")) {
//                     return@settingsEvaluated
//                 }
//
//                 // java.home
//
//                 if (JavaVersion.current() !== JavaVersion.VERSION_21) {
//                     throw GradleException("This build requires JDK 21 or higher. It's currently ${getBuildJavaHome()}.")
//                 }
//             }

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


            // val foundJava = System.getProperty("java.home")
            // val otherJavaLocation = System.getenv("JAVA_HOME")
            // val hasJava = foundJava != null || otherJavaLocation != null
            // val validJava = hasJava && JavaVersion.VERSION_1_7 == JavaVersion.VERSION_1_7

            // if(!validJava) {
            //     println("Found version: ${foundJava}")
            //     val message = """
            //                     This build requires Java 17, currently using ${JavaVersion.VERSION_1_7}. Please also ensure JAVA_HOME is configured correctly (ie echo ${'$'}{JAVA_HOME} should return a result"
            //                 """.trimIndent()
            //
            //     throw Exception(
            //         message
            //     )
            // }

            // plugins.apply(DevelocityPlugin::class.java)
            //
            // println("Build cache hashcode from component plugin is: ${buildCache.hashCode()}")
            // println("Settings Build cache hashcode from component plugin is: ${buildCache.hashCode()}")
            //
            // plugins.withType(DevelocityPlugin::class.java) {
            //     extensions.findByType(DevelocityPlugin::class.java).run {
            //
            //         develocity.run {
            //             buildScan.run {
            //                 publishing.onlyIf {
            //                     val hasFailures = it.buildResult.failures.isNotEmpty()
            //                     println("Decision to publish build scan was: ${hasFailures}.")
            //                     if(hasFailures) {
            //                         println("Failures were: ${it.buildResult.failures}")
            //                     }
            //                     hasFailures
            //                 }
            //
            //                 uploadInBackground.set(false)
            //                 termsOfUseUrl.set("https://gradle.com/terms-of-service")
            //                 termsOfUseAgree.set("yes")
            //
            //                 /*
            //                     If you try to abstract these strings to a 'val' outside gradleEnterprise lambda, you get errors
            //                     such as below:
            //
            //                     4 problems were found storing the configuration cache.
            //                     - Gradle runtime: cannot serialize Gradle script object references as these are not supported with the configuration cache.
            //                       See https://docs.gradle.org/8.5/userguide/configuration_cache.html#config_cache:requirements:disallowed_types
            //                         ...
            //                         ...
            //                  */
            //                 obfuscation.run {
            //                     username { "OBFUSCATED_USERNAME" }
            //                     ipAddresses { addresses -> addresses.map { _ -> "OBFUSCATED_IP_ADDRESS" } }
            //                     hostname { "OBFUSCATED_HOSTNAME" }
            //                 }
            //             }
            //         }
            //
            //         buildCache.run {
            //             local.run {
            //                 isEnabled = true
            //             }
            //
            //             remote(HttpBuildCache::class.java   ).run {
            //                 isEnabled = false // turn on in future
            //                 isPush = true
            //                 isAllowUntrustedServer = true
            //                 isAllowInsecureProtocol = true
            //                 url = URI("http://localhost:5071/cache/")
            //                 credentials.run {
            //                     username = "tom"
            //                     password = "Password01!" // used for showcase
            //                 }
            //             }
            //         }
            //     }
            // }
            //
            //
            //
            // fixUndeclaredBuildServiceUsageWarning()
            //
            // enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
            // enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
        }
    }

    /*
         Fixes undeclared build service usage when using: settings.enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
         Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165

         Note: This is done using beforeProject so that all tasks are available and be correctly configured. It also
         means that each project is done in isolation correctly, and parallelism can be done properly.
     */
    private fun Settings.fixUndeclaredBuildServiceUsageWarning() {
        gradle.lifecycle.beforeProject {
            val tasksOfInterest = project.tasks.withType(DefaultTask::class.java)

            tasksOfInterest.configureEach {
                project.gradle.sharedServices.registrations.all {
                    val buildServiceProvider = this.service
                    val buildService = buildServiceProvider.get()

                    val kotlinCollectorSearchString =
                        "org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector"
                    if (buildService.toString().contains(kotlinCollectorSearchString)) {
                        project.logger.debug(
                            "Applying checkKotlinGradlePluginConfigurationErrors workaround to task: {} for project: {}",
                            this@configureEach.name,
                            project.name
                        )
                        this@configureEach.usesService(buildServiceProvider)
                    }
                }
            }
        }
    }
}
