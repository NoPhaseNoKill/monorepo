rootProject.name = "build-logic"

pluginManagement {
    includeBuild("convention-plugins")
}

dependencyResolutionManagement {
    versionCatalogs {
        val catalog = maybeCreate(defaultLibrariesExtensionName.get())
        catalog.from(files(file("../gradle/libs.versions.toml")))
    }
}
