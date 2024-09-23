rootProject.name = "included-build-plugins-that-apply-binary-plugins"

/*
    Note: Don't implement a blank build.gradle file here. Otherwise it'll pull in random deps
    for the gradle plugin, and in doing so will mean you need to declare dependencies at the top level
    which then defeats the purpose of convention/binary plugins
 */

include("included-build-plugins-that-apply-binary-plugins-plugin-one")
include("included-build-plugins-that-apply-binary-plugins-plugin-two")

dependencyResolutionManagement {

    repositories {
        versionCatalogs {
            maybeCreate(defaultLibrariesExtensionName.get()).from(files(file("../../gradle/libs.versions.toml")))
        }
    }

}
