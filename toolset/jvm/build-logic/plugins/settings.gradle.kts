dependencyResolutionManagement {
    // Reuse version catalog from the main build.
    versionCatalogs {
        create("libs") { from(files("../../gradle/libs.versions.toml")) }
    }
}

rootProject.name = "plugins"

include("kotlin-common-conventions")
include("kotlin-application-conventions")
include("kotlin-library-conventions")