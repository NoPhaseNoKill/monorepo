rootProject.name = "generalised-platform"

dependencyResolutionManagement {
    versionCatalogs {
        create(defaultLibrariesExtensionName.get()) {
            from(files("../../../gradle/libs.versions.toml"))
        }
    }
}