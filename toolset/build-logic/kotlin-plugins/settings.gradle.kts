rootProject.name = "kotlin-plugins"

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

includeBuild("../meta-byte-buddy")
includeBuild("../meta-gradle-utilities")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
