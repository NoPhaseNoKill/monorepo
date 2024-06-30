rootProject.name = "standalone-plugin-one"

dependencyResolutionManagement {
    versionCatalogs {
        create(defaultLibrariesExtensionName.get()) {
            from(files("../../.././gradle/libs.versions.toml"))
        }
    }
}