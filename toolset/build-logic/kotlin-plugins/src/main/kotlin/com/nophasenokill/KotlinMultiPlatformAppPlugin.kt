package com.nophasenokill

import app.cash.sqldelight.gradle.SqlDelightExtension
import com.nophasenokill.extensions.*
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin

class KotlinMultiPlatformAppPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {

            version = "1.2.0"

            val versionCatalog = project.findCatalog()
            pluginManager.apply(versionCatalog.findCatalogPlugin("kotlinMultiplatform"))
            pluginManager.apply(versionCatalog.findCatalogPlugin("kotlinSerialization"))
            pluginManager.apply(versionCatalog.findCatalogPlugin("jetbrainsCompose"))
            pluginManager.apply(versionCatalog.findCatalogPlugin("sqldelight"))
            pluginManager.apply(versionCatalog.findCatalogPlugin("composeCompiler"))

            kotlin {
                jvm()
                sourceSets {

                    all {
                        dependencies {
                            implementation(project.dependencies.platform(versionCatalog.findCatalogLibrary("kotlin.bom")))
                            implementation(project.dependencies.platform(versionCatalog.findCatalogLibrary("kotlinx.coroutines.bom")))
                            implementation(project.dependencies.platform(versionCatalog.findCatalogLibrary("kotlinx.serialization.bom")))
                            implementation(project.dependencies.platform(versionCatalog.findCatalogLibrary("ktor.bom")))
                        }
                    }

                    jvmMain.dependencies {
                        implementation(versionCatalog.findCatalogLibrary("gradle.tooling.api"))
                        implementation(versionCatalog.findCatalogLibrary("gradle.declarative.dsl.tooling.models"))
                        implementation(versionCatalog.findCatalogLibrary("gradle.declarative.dsl.core"))
                        implementation(versionCatalog.findCatalogLibrary("sqldelight.extensions.coroutines"))
                        implementation(versionCatalog.findCatalogLibrary("sqldelight.runtime"))
                        implementation(versionCatalog.findCatalogLibrary("sqldelight.driver.sqlite"))
                        implementation(versionCatalog.findCatalogLibrary("decompose.decompose"))
                        implementation(versionCatalog.findCatalogLibrary("decompose.compose"))
                        implementation(versionCatalog.findCatalogLibrary("essenty.lifecycle.coroutines"))
                        implementation(versionCatalog.findCatalogLibrary("kotlinx.serialization.json"))
                        implementation(versionCatalog.findCatalogLibrary("ktor.client.okhttp"))
                        implementation(versionCatalog.findCatalogLibrary("ktor.serialization.kotlinx.json"))
                        implementation(versionCatalog.findCatalogLibrary("material3WindowSizeClassMultiplatform"))
                        implementation(versionCatalog.findCatalogLibrary("materialKolor"))
                        implementation(versionCatalog.findCatalogLibrary("slf4j.api"))
                        implementation(versionCatalog.findCatalogLibrary("logback.classic"))
                        implementation(versionCatalog.findCatalogLibrary("gradle.declarative.dsl.core"))
                        implementation(versionCatalog.findCatalogLibrary("gradle.declarative.dsl.evaluator"))
                        implementation(versionCatalog.findCatalogLibrary("gradle.declarative.dsl.tooling.models"))


                        implementation(compose.runtime)
                        implementation(compose.foundation)
                        implementation(compose.material3)
                        implementation(compose.materialIconsExtended)
                        implementation(compose.ui)
                        implementation(compose.components.resources)
                        implementation(compose.components.uiToolingPreview)
                        implementation(compose.desktop.currentOs)


                        runtimeOnly(versionCatalog.findCatalogLibrary("kotlinx.coroutines.swing"))
                    }

                    jvmTest.dependencies {
                        implementation(versionCatalog.findCatalogLibrary("junit.junit"))
                        implementation(compose.desktop.uiTestJUnit4)
                    }
                }
            }


            sqldelight {

                databases.create("ApplicationDatabase") {
                    packageName.set("com.nophasenokill.client.core.database.sqldelight.generated")
                    verifyDefinitions.set(true)
                    verifyMigrations.set(true)
                    deriveSchemaFromMigrations.set(true)
                    generateAsync.set(false)
                }
            }

            val appName = "GradleClient"
            val appDisplayName = "Gradle Client"
            val appQualifiedName = "com.nophasenokill.client"
            val appUUID = file("app-uuid.txt").readText().trim()

            compose.desktop {
                application {
                    mainClass = "com.nophasenokill.client.GradleClientMainKt"
                    jvmArgs += "-Xms35m"
                    jvmArgs += "-Xmx128m"
                    javaHome = javaToolchains.compilerFor {}.map { it.metadata.installationPath }.get().asFile.path

                    buildTypes.release.proguard {
                        optimize.set(false)
                        obfuscate.set(false)
                        configurationFiles.from(layout.projectDirectory.file("proguard-desktop.pro"))
                    }

                    nativeDistributions {
                        targetFormats(
                            org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                            org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                            org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
                        )
                        packageName = appName
                        packageVersion = project.version.toString()
                        description = appDisplayName
                        vendor = "Gradle"
                        copyright = "© ${java.time.Year.now()} the original author or authors."
                        appResourcesRootDir.set(layout.projectDirectory.dir("src/assets"))
                        jvmArgs += "-splash:${'$'}APPDIR/resources/splash.png"
                        modules(
                            "java.management",
                            "java.naming",
                            "java.sql",
                        )
                        linux {
                            iconFile.set(layout.projectDirectory.file("src/assets/desktop/icon.png"))
                        }
                        macOS {
                            appStore = false
                            bundleID = appQualifiedName
                            dockName = appDisplayName
                            iconFile.set(layout.projectDirectory.file("src/assets/desktop/icon.icns"))
                        }
                        windows {
                            menu = true
                            menuGroup = "" // root
                            perUserInstall = true
                            // https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                            upgradeUuid = appUUID
                            iconFile.set(layout.projectDirectory.file("src/assets/desktop/icon.ico"))
                        }
                    }
                }
            }
            repositories {
                mavenCentral()
                gradlePluginPortal {
                    content {
                        includeGroupAndSubgroups("com.gradle")
                        includeGroupAndSubgroups("org.gradle")
                        includeGroupAndSubgroups("io.github.gradle")
                    }
                }
                google {
                    content {
                        includeGroupAndSubgroups("androidx")
                        includeGroupAndSubgroups("com.android")
                        includeGroupAndSubgroups("com.google")
                    }
                }
                gradlePluginPortal {
                    content {
                        includeGroup("org.gradle.toolchains")
                        includeGroup("org.gradle.experimental")
                    }
                }
                maven(url = "https://repo.gradle.org/gradle/libs-releases") {
                    content {
                        includeGroup("org.gradle")
                    }
                }
                maven(url = "https://repo.gradle.org/gradle/libs-snapshots") {
                    content {
                        includeGroup("org.gradle")
                    }
                }
                mavenCentral()
            }
        }
    }
}

val Project.javaToolchains: org.gradle.jvm.toolchain.JavaToolchainService
    get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("javaToolchains") as org.gradle.jvm.toolchain.JavaToolchainService

val org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.compose: org.jetbrains.compose.ComposePlugin.Dependencies
    get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("compose") as org.jetbrains.compose.ComposePlugin.Dependencies

val Project.sqldelight: SqlDelightExtension
    get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("sqldelight") as SqlDelightExtension

fun Project.sqldelight(configure: Action<SqlDelightExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("sqldelight", configure)

fun ComposeExtension.desktop(configure: Action<org.jetbrains.compose.desktop.DesktopExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("desktop", configure)

val ComposeExtension.desktop: org.jetbrains.compose.desktop.DesktopExtension
    get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("desktop") as org.jetbrains.compose.desktop.DesktopExtension

val Project.compose: ComposeExtension
    get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("compose") as ComposeExtension

val org.gradle.api.artifacts.dsl.DependencyHandler.compose: ComposePlugin.Dependencies
    get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("compose") as ComposePlugin.Dependencies
