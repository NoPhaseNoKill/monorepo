rootProject.name = "jvm"

pluginManagement {
    includeBuild("../build-logic")
}
/*
    DO not use dependency management here. If you need to add more repositories,
    add them to: com.nophasenokill.component-plugin.settings.gradle.kts
 */

plugins {
    id("com.nophasenokill.component-plugin")
}