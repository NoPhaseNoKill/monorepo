rootProject.name = "root-settings-plugin"

pluginManagement {
    includeBuild("../build-service-warning-fix-plugin")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
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

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")