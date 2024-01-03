rootProject.name = "integraboost"

pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("integraboost.plugin.root.monorepo-modules-structure")
    id("integraboost.plugin.root.build-scan-plugin")
}