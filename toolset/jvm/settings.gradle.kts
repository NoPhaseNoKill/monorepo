rootProject.name = "jvm"

pluginManagement {
    includeBuild("../build-logic")
    includeBuild("../build-logic-meta")
}
/*
    DO not use dependency management here. If you need to add more repositories,
    add them to: com.nophasenokill.component-plugin.settings.gradle.kts
 */

plugins {
    id("com.nophasenokill.component-plugin")
}

settings.enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
settings.enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
