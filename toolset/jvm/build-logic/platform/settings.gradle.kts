dependencyResolutionManagement {

    // explicitly gradle plugin portal because we only want to search for our convention plugins,
    // where the convention plugins delegate the dependency retrieval to the platform
    repositories.gradlePluginPortal()
    //
    versionCatalogs {
        create("libs") {
            from(files("../../gradle/libs.versions.toml"))
        }
    }
}


rootProject.name = "platform"

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")