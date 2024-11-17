rootProject.name = "meta-byte-buddy"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
    versionCatalogs {
        val catalog = maybeCreate(defaultLibrariesExtensionName.get())
        catalog.from(files(file("../gradle/libs.versions.toml")))
    }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
