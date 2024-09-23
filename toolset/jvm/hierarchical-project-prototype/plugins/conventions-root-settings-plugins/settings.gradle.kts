rootProject.name = "conventions-root-settings-plugins"

include("conventions-root-settings-plugins-plugin-one")

dependencyResolutionManagement {
    repositories {
        versionCatalogs {
            maybeCreate(defaultLibrariesExtensionName.get()).from(files(file("../../gradle/libs.versions.toml")))
        }
    }
}
