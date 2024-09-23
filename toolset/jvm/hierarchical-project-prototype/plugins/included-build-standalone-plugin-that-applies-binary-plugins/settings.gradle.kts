rootProject.name = "included-build-standalone-plugin-that-applies-binary-plugins"

dependencyResolutionManagement {

    repositories {
        versionCatalogs {
            maybeCreate(defaultLibrariesExtensionName.get()).from(files(file("../../gradle/libs.versions.toml")))
        }
    }

}
