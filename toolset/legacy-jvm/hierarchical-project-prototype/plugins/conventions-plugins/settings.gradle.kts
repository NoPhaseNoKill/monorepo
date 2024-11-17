rootProject.name = "conventions-plugins"

include("conventions-plugin-one")
include("conventions-plugin-two")

dependencyResolutionManagement {
    repositories {
        versionCatalogs {
            maybeCreate(defaultLibrariesExtensionName.get()).from(files(file("../../gradle/libs.versions.toml")))
        }
    }
}
