package com.nophasenokill

import app.cash.sqldelight.gradle.SqlDelightExtension
import com.nophasenokill.extensions.findCatalogLibrary
import com.nophasenokill.extensions.javaToolchains
import com.nophasenokill.extensions.kotlinMpp
import com.nophasenokill.extensions.sourceSets
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.ComposePlugin.DesktopDependencies
import org.jetbrains.compose.desktop.DesktopExtension

class KotlinMultiPlatformAppPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {

            val versionCatalog = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

            /*
                TODO At some stage we should be using the kotlin-base-plugin, and adding the KMP over the top,
                however at the moment we get:
                    > Failed to apply plugin 'org.jetbrains.kotlin.multiplatform'.
                    > Cannot add extension with name 'kotlin', as there is an extension already registered with that name.

             */
            pluginManager.apply(versionCatalog.findPlugin("kotlinMultiplatform").get().get().pluginId)
            pluginManager.apply(versionCatalog.findPlugin("kotlinSerialization").get().get().pluginId)
            pluginManager.apply(versionCatalog.findPlugin("sqldelight").get().get().pluginId)
            pluginManager.apply(versionCatalog.findPlugin("composeCompiler").get().get().pluginId)

            kotlinMpp {
                /*
                       Adds a task named 'test' so that we can use this in the testAll from the root jvm project.

                       For some reason, MPP breaks conventions of having a 'test' task and changes it to 'allTests',
                       so we need to manually make this.

                       Note: 'allTests' provides a report aggregation which we don't want, so instead we need to iterate
                       over each one.
                */
                val testTask = tasks.register("test") {
                    group = "verification"
                    description = "Runs all tests for all targets."

                    /*
                         Ensures we depend on all platform test tasks aka jvmTest, iosTest etc
                      */
                    targets.all {
                        val targetTestTask = tasks.findByName("${name}Test")
                        if (targetTestTask != null) {
                            dependsOn(targetTestTask)
                        }
                    }
                }

                tasks.named("check") {
                    dependsOn(testTask)
                }

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

                        val composePluginVersion = versionCatalog.findVersion("composePlugin").get()

                        implementation("org.jetbrains.compose.foundation:foundation:${composePluginVersion}")
                        implementation("org.jetbrains.compose.material3:material3:${composePluginVersion}")
                        implementation("org.jetbrains.compose.material:material-icons-extended:${composePluginVersion}")
                        implementation("org.jetbrains.compose.ui:ui:${composePluginVersion}")
                        implementation("org.jetbrains.compose.components:components-resources:${composePluginVersion}")
                        implementation("org.jetbrains.compose.components:components-ui-tooling-preview:${composePluginVersion}")
                        implementation(DesktopDependencies.currentOs)

                        runtimeOnly(versionCatalog.findCatalogLibrary("kotlinx.coroutines.swing"))

                        jvmTest.dependencies {
                            implementation(versionCatalog.findCatalogLibrary("junit.junit"))
                            implementation("org.jetbrains.compose.ui:ui-test-junit4:${composePluginVersion}")
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

                val composeDesktopApplicationExtension = extensions.findByType(DesktopExtension::class.java)

                composeDesktopApplicationExtension?.application {
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
}


val org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.compose: ComposePlugin.Dependencies
    get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("compose") as ComposePlugin.Dependencies

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
