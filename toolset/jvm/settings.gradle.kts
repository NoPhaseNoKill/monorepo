rootProject.name = "jvm"

pluginManagement {
    /*
        Including these here allows us access for plugins and prevents them being unable to be
        found.
     */
    includeBuild("known-to-be-working-examples/plugins/root-settings-plugin")
    includeBuild("known-to-be-working-examples/plugins/hash-source-plugin")
    repositories {
        gradlePluginPortal() // Allows us local plugins to be found
    }
}

dependencyResolutionManagement {
    rulesMode = RulesMode.FAIL_ON_PROJECT_RULES
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.nophasenokill.root-settings-plugin") version("4.3.1")
}

include("known-to-be-working-examples:plugins:basic-plugin")
include("known-to-be-working-examples:plugins:extended-plugin")

