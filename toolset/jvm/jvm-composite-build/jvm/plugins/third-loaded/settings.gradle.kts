
rootProject.name = "third-loaded"

pluginManagement {
    includeBuild("../first-loaded")
    includeBuild("../second-loaded")
}

includeBuild("../second-loaded")

plugins {
    id("first-loaded-plugin")
}
