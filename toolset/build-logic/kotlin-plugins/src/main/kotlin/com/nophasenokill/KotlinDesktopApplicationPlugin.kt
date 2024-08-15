package com.nophasenokill

import com.nophasenokill.extensions.findCatalog
import com.nophasenokill.extensions.findCatalogLibrary
import com.nophasenokill.extensions.kotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import java.net.URI
import java.util.*

class KotlinDesktopApplicationPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        project.run {

            if(!project.pluginManager.hasPlugin("com.nophasenokill.kotlin-base-plugin")) {
                project.pluginManager.apply("com.nophasenokill.kotlin-base-plugin")
            }

            val versionCatalog = project.findCatalog()

            pluginManager.apply(versionCatalog.findPlugin("jetbrainsCompose").get().get().pluginId)
            pluginManager.apply(versionCatalog.findPlugin("composeCompiler").get().get().pluginId)


            repositories {
                mavenCentral()
                maven {
                    url = URI("https://maven.pkg.jetbrains.space/public/p/compose/dev")
                }
                google()
            }


            kotlinJvm {
                KotlinPlatformType.jvm
            }

            dependencies {
                // Note, if you develop a library, you should use compose.desktop.common.
                // compose.desktop.currentOs should be used in launcher-sourceSet
                // (in a separate module for demo project and in testMain).
                // With compose.desktop.common you will also lose @Preview functionality
                add("implementation", compose.desktop.currentOs)
                add("implementation", compose.material3)
                add("implementation", compose.materialIconsExtended)
                add("implementation", compose.material)
                add("implementation", compose.uiUtil)
                add("implementation", compose.materialIconsExtended)

                add("api", compose.runtime)
                add("api", compose.foundation)
                add("api", versionCatalog.findCatalogLibrary("materialKolor"))

            }

            val mainClassName = projectDir.name.split("-").joinToString("") { it ->
                it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            } + "AppKt"

            compose.desktop.run {
                application {
                    mainClass = ("com.nophasenokill.${mainClassName}")

                    nativeDistributions {
                        targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                        packageName =  projectDir.name
                        /*
                            Version must be strictly x.y.z and >= 1.0.0
                            for native packaging to work across platforms
                         */
                        packageVersion = "1.0.0"
                    }
                }
            }
        }
    }

    val org.jetbrains.compose.ComposeExtension.desktop: org.jetbrains.compose.desktop.DesktopExtension get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("desktop") as org.jetbrains.compose.desktop.DesktopExtension

    val Project.compose: org.jetbrains.compose.ComposeExtension get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("compose") as org.jetbrains.compose.ComposeExtension

    val org.gradle.api.artifacts.dsl.DependencyHandler.compose: ComposePlugin.Dependencies get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("compose") as ComposePlugin.Dependencies
}




