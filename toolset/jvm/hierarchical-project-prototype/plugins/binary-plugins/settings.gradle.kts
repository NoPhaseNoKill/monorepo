rootProject.name = "binary-plugins"

include("binary-plugin-one")
include("binary-plugin-two")

dependencyResolutionManagement {
    repositories {
        versionCatalogs {
            maybeCreate(defaultLibrariesExtensionName.get()).from(files(file("../../gradle/libs.versions.toml")))
        }
    }
}
