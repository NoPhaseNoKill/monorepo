rootProject.name = "binary-root-settings-plugins"

include("binary-root-settings-plugin-one")

dependencyResolutionManagement {
    repositories {
        versionCatalogs {
            maybeCreate(defaultLibrariesExtensionName.get()).from(files(file("../../gradle/libs.versions.toml")))
        }
    }
}
