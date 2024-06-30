rootProject.name = "standalone-projects"


plugins {
    id("com.nophasenokill.root-settings-plugin")
}

/*
    The root settings declares this, but in the wrong place relative to root.

    TODO investigate how to make root-settings-plugin relative to root
 */

dependencyResolutionManagement {
    versionCatalogs {
        create(defaultLibrariesExtensionName.get()) {
            from(files("../../gradle/libs.versions.toml"))
        }
    }
}

includeBuild("../plugins/basic-kotlin-plugin")

include("library-one")
include("application-one")
