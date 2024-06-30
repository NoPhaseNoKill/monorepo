rootProject.name = "basic-plugin"

pluginManagement {
    /*
        This implicitly also brings in:
            plugins {
               id("org.jetbrains.kotlin.jvm") version("1.9.22")
            }
     */
    includeBuild("../root-settings-plugin")
    includeBuild("../build-service-warning-fix-plugin")
}

plugins {
    id("com.nophasenokill.root-settings-plugin")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    versionCatalogs {
        create(defaultLibrariesExtensionName.get()) {
            from(files("../../../gradle/libs.versions.toml"))
        }
    }
}