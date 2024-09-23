plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "kotlin-plugin-one"

dependencyResolutionManagement {

    repositories {
        versionCatalogs {
            maybeCreate(defaultLibrariesExtensionName.get()).from(files(file("../../../gradle/libs.versions.toml")))
        }
    }
}

