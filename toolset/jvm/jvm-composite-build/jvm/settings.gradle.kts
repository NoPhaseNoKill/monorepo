
rootProject.name = "jvm"

pluginManagement {
    includeBuild("plugins/first-loaded")
    includeBuild("plugins/second-loaded")
    includeBuild("plugins/third-loaded")
}


includeBuild("plugins/first-loaded")
includeBuild("plugins/second-loaded")
includeBuild("plugins/third-loaded")

plugins {
    id("first-loaded-plugin")
}
