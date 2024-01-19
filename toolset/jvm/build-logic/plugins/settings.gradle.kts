pluginManagement {
    includeBuild("../settings")
}

plugins {
    id("kotlin-project-root-repositories")
}

rootProject.name = "plugins"

include("commons-plugin")
include("kotlin-library-plugin")
include("kotlin-application-plugin")
include("dependency-analysis-plugins")