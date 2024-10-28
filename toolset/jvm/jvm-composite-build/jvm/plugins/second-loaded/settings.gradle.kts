
rootProject.name = "second-loaded"

pluginManagement {
    includeBuild("../first-loaded")
}

plugins {
    id("first-loaded-plugin")
}
