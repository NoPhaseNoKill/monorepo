pluginManagement {
    includeBuild("../settings")
}

plugins {
    id("kotlin-project-root-settings")
}

rootProject.name = "plugins"

include("commons-plugin")
include("kotlin-library-plugin")
include("kotlin-application-plugin")
include("dependency-analysis-plugins")