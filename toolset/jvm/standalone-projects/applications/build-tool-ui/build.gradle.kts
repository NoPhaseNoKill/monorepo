import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.time.Year

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.composeCompiler)
}

group = "com.nophasenokill.client"

// Version must be strictly x.y.z and >= 1.0.0
// for native packaging to work across platforms
version = "1.2.0"

val appName = "GradleClient"
val appDisplayName = "Gradle Client"
val appQualifiedName = "com.nophasenokill.client"
val appUUID = file("app-uuid.txt").readText().trim()

kotlin {
    jvm()
    sourceSets {

        all {
            dependencies {
                implementation(project.dependencies.platform(libs.kotlin.bom))
                implementation(project.dependencies.platform(libs.kotlinx.coroutines.bom))
                implementation(project.dependencies.platform(libs.kotlinx.serialization.bom))
                implementation(project.dependencies.platform(libs.ktor.bom))
            }
        }

        jvmMain.dependencies {

            implementation(libs.gradle.tooling.api)

            implementation(libs.gradle.tooling.api)
            implementation(libs.gradle.declarative.dsl.tooling.models)

            implementation(libs.gradle.declarative.dsl.core)

            implementation(libs.sqldelight.extensions.coroutines)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.driver.sqlite)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.desktop.currentOs)

            implementation(libs.decompose.decompose)
            implementation(libs.decompose.compose)
            implementation(libs.essenty.lifecycle.coroutines)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.material3WindowSizeClassMultiplatform)
            implementation(libs.materialKolor)

            implementation(libs.slf4j.api)
            implementation(libs.logback.classic)

            implementation(libs.gradle.declarative.dsl.core)
            implementation(libs.gradle.declarative.dsl.evaluator)
            implementation(libs.gradle.declarative.dsl.tooling.models)

            runtimeOnly(libs.kotlinx.coroutines.swing)
        }

        jvmTest.dependencies {
            implementation(libs.junit.junit)
            implementation(compose.desktop.uiTestJUnit4)
        }
    }
}

sqldelight {
    databases {
        create("ApplicationDatabase") {
            packageName = "com.nophasenokill.client.core.database.sqldelight.generated"
            verifyDefinitions = true
            verifyMigrations = true
            deriveSchemaFromMigrations = true
            generateAsync = false
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.nophasenokill.client.GradleClientMainKt"
        jvmArgs += "-Xms35m"
        jvmArgs += "-Xmx128m"
        javaHome = javaToolchains.compilerFor{}.map { it.metadata.installationPath }.get().asFile.path

        buildTypes.release.proguard {
            optimize = false
            obfuscate = false
            configurationFiles.from(layout.projectDirectory.file("proguard-desktop.pro"))
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = appName
            packageVersion = project.version.toString()
            description = appDisplayName
            vendor = "Gradle"
            copyright = "© ${Year.now()} the original author or authors."
            appResourcesRootDir = layout.projectDirectory.dir("src/assets")
            jvmArgs += "-splash:${'$'}APPDIR/resources/splash.png"
            modules(
                "java.management",
                "java.naming",
                "java.sql",
            )
            linux {
                iconFile = layout.projectDirectory.file("src/assets/desktop/icon.png")
            }
            macOS {
                appStore = false
                bundleID = appQualifiedName
                dockName = appDisplayName
                iconFile = layout.projectDirectory.file("src/assets/desktop/icon.icns")
            }
            windows {
                menu = true
                menuGroup = "" // root
                perUserInstall = true
                // https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                upgradeUuid = appUUID
                iconFile = layout.projectDirectory.file("src/assets/desktop/icon.ico")
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
