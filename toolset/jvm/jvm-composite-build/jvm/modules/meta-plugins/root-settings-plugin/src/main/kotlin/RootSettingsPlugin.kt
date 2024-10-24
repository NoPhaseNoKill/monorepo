import com.gradle.develocity.agent.gradle.DevelocityPlugin
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.PluginAware
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmImplementation
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.kotlin.dsl.develocity
import org.gradle.toolchains.foojay.FoojayToolchainsPlugin
import java.io.File
import java.net.URI
import java.nio.file.Paths

class RootSettingsPlugin : Plugin<Settings> {

    override fun apply(settings: Settings) {



        settings.run {

            println("Hash code: ${settings.getBuildscript().hashCode()}")
            println("Hash code: ${buildscript.hashCode()}")

            /*
                      gradle. projectsLoaded {
                        rootProject.buildscript {
                          repositories {
                            //...
                          }
                          dependencies {
                            //...
                          }
                        }
                      }
             */
            // // settings.gradle.projectsLoaded { project ->
            //     settings.buildscript.configurations.all { configuration ->
            //         if(configuration.name == "classpath") {
            //             configuration.allDependencyConstraints.find { it.name.contains("stdlib") }?.version { version ->
            //                 version.strictly("2.0.21") }
            //             val constraints =configuration.allDependencyConstraints
            //             constraints.forEach { constraint ->
            //                 constraint.version { constraint ->
            //                     constraint.strictly("2.0.21")
            //                 }
            //             }
            //             println("Config build deps: ${configuration.allDependencyConstraints}")
            //             // this.buildscript.dependencies.constraints.add("classpath", "org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
            //             // println("Hierarchy: ${configuration.getHierarchy()}")
            //             // val resolved = configuration.resolve()
            //
            //         }
            //     }
            // // }

            // settings.gradle.settingsEvaluated {
            //     this.buildscript.configurations.all { configuration ->
            //         println("Configuration: ${configuration.name}")
            //     }
            // }


            settings.buildscript.configurations.all { configuration ->
                // configuration.resolutionStrategy.forcedModules.forEach { forcedModule ->
                //     if(forcedModule.name.contains("kotlin-stdlib")) {
                //         forcedModule.apply {  }
                //     }
                // }
                println("Forced modules for ${configuration.name}: ${configuration.resolutionStrategy.forcedModules}")
            }

            pluginManagement {
                buildscript.apply {
                    configurations.all { configuration ->
                        configuration.resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
                    }
                }
            }

            settings.gradle.settingsEvaluated {

                val rootPath = Paths.get(settings.rootDir.absolutePath)
                val settingsPath = Paths.get(settings.settingsDir.absolutePath)
                val environmentVariable = settings.providers.environmentVariable("PUBLISHING_REPOSITORY_URL")

                // Compute the relative path from the settings directory to the root directory
                val relativePath = settingsPath.relativize(rootPath).toString()
                val repo = environmentVariable.getOrElse("$relativePath/local-repo")

                settings.pluginManagement.apply {
                    repositories.apply {

                        maven { action ->
                            action.setUrl(
                                URI(repo)
                            )
                        }
                    }

                    // Enforce Kotlin version for plugins
                    resolutionStrategy.apply {
                        eachPlugin { plugin ->
                            if (plugin.requested.id.id.startsWith("org.jetbrains.kotlin")) {
                                plugin.useVersion("2.0.21")
                            }
                        }
                    }
                }
            }

            configureBuildScriptRepo()
            configureMavenLocalRepoPlugin()
            configureSubprojectsWithJavaToolchain()
            configureSubprojectsWithBasePlugin()
            configureBuildStructure()
            configureDevelocity()
            configureFoojayResolver()
        }
    }
    private fun Settings.configureBuildScriptRepo() {
        settings.buildscript.repositories.apply {
            maven { action ->
                action.setUrl(
                    providers.environmentVariable("PUBLISHING_REPOSITORY_URL")
                        .getOrElse("/home/gardo/projects/monorepo/toolset/jvm/jvm-composite-build/jvm/local-repo")
                )
            }
            gradlePluginPortal()
        }
    }

    private fun Settings.configureMavenLocalRepoPlugin() {
        pluginManagement.run {
            resolutionStrategy.eachPlugin { plugin ->
                if (plugin.requested.id.id.startsWith("org.jetbrains.kotlin")) {
                    plugin.useVersion("2.0.21")
                }
            }

            repositories.apply {
                maven { action ->
                    action.setUrl(
                        providers.environmentVariable("PUBLISHING_REPOSITORY_URL")
                            .getOrElse("/home/gardo/projects/monorepo/toolset/jvm/jvm-composite-build/jvm/local-repo")
                    )
                }
                gradlePluginPortal()
            }
        }
    }

    private fun Settings.configureSubprojectsWithJavaToolchain() {

        val javaExtension = extensions.findByType(JavaPluginExtension::class.java)

        gradle.lifecycle.beforeProject { project ->
            project.pluginManager.withPlugin("org.gradle.java-base") {
                javaExtension?.apply{

                    toolchain {
                        it.languageVersion.set(JavaLanguageVersion.of(21))
                        it.vendor.set(JvmVendorSpec.ADOPTIUM)
                        it.implementation.set(JvmImplementation.VENDOR_SPECIFIC)
                    }

                    consistentResolution {
                        it.useCompileClasspathVersions()
                    }

                    withSourcesJar()
                    withJavadocJar()
                }
            }
        }
    }

    private fun Settings.configureSubprojectsWithBasePlugin() {
        gradle.lifecycle.beforeProject { project ->
            if(project.name !== "jvm" && project.name !== "root-settings-plugin") {
                project.apply ("plugin" to "base")
                project.apply("plugin" to "com.nophasenokill.maven-local-repo-plugin")
            }
        }
    }

    private fun Settings.configureBuildStructure() {

        val exclusions = listOf(ProjectCategory.META_PLUGINS)
        println("Excluding: ${MetaPluginName.entries.joinToString { "${it.name}," }} as they have already been included in the root build")

        fun includeProject(customProject: CustomProject) {

            val type: ProjectCategory = customProject.category
            val pluginName: String = customProject.path

            include(":$pluginName")
            project(":$pluginName").projectDir = File("${type.path}/${customProject.path}")
        }

        combined
            .filter { !exclusions.contains(it.category) }
            .map {
                println("Including project: ${it}")
                includeProject(it)
            }

    }

    private fun Settings.configureFoojayResolver() {
        pluginManager.apply(FoojayToolchainsPlugin::class.java)
    }

    private fun Settings.configureDevelocity() {

        pluginManager.apply(DevelocityPlugin::class.java)

        pluginManager.withPlugin("com.gradle.develocity") {

            develocity {
                buildScan.apply {

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

                    obfuscation.apply {
                        username { OBFUSCATED_USERNAME }
                        ipAddresses { addresses -> addresses.map { _ -> OBFUSCATED_HOSTNAME } }
                        hostname { OBFUSCATED_IP_ADDRESS }
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

enum class MetaPluginName(
    override val path: String
): CustomProject {
    ROOT_SETTINGS_PLUGIN("root-settings-plugin");

    override val category = ProjectCategory.META_PLUGINS
}

enum class ApplicationName(
    override val path: String
): CustomProject {
    APPLICATION_ONE("application-one");

    override val category = ProjectCategory.APPLICATIONS
}

enum class LibraryName(override val path: String):
    CustomProject {
    LIBRARY_ONE("library-one");

    override val category = ProjectCategory.LIBRARIES
}

enum class DependencyName(override val path: String):
    CustomProject {
    BASE_DEPENDENCIES("base-dependencies");

    override val category = ProjectCategory.DEPENDENCIES
}

enum class PluginName(override val path: String):
    CustomProject {
    JAVA_BASE_PLUGIN("java-base-plugin"),
    KOTLIN_BASE_PLUGIN("kotlin-base-plugin"),
    REPOSITORIES_PLUGIN("repositories-plugin");

    override val category = ProjectCategory.PLUGINS
}

interface CustomProject {
    val path: String
    val category: ProjectCategory
}

enum class ProjectCategory(val path: String) {
    APPLICATIONS("modules/applications"),
    LIBRARIES("modules/libraries"),
    DEPENDENCIES("modules/dependencies"),
    META_PLUGINS("modules/meta-plugins"),
    PLUGINS("modules/plugins");
}

val combined: Set<CustomProject> =
    ApplicationName.entries.toSet() +
            LibraryName.entries.toSet() +
            DependencyName.entries.toSet() +
            PluginName.entries.toSet()


fun PluginAware.apply(vararg options: Pair<String, Any?>) {
    apply(mapOf(*options))
}
