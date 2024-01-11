pluginManagement {
    includeBuild("../settings")
}

plugins {
    id("kotlin-project-root-settings")
}

rootProject.name = "platforms"

include("plugins-platform")
include("test-platform")