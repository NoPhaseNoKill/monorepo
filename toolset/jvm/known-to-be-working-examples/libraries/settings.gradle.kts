rootProject.name = "libraries"

pluginManagement {
    includeBuild("../plugins/root-settings-plugin")
    includeBuild("../plugins/build-service-warning-fix-plugin")
    includeBuild("../plugins/extended-plugin")
}

plugins {
    id("com.nophasenokill.root-settings-plugin")
}

include("library-one")
