rootProject.name = "jvm"

pluginManagement {
    includeBuild("known-to-be-working-examples/plugins/root-settings-plugin")
}

includeBuild("known-to-be-working-examples/domain")
includeBuild("known-to-be-working-examples/plugins/hash-source-plugin")
include("known-to-be-working-examples:plugins:basic-plugin")
include("known-to-be-working-examples:plugins:extended-plugin")


dependencyResolutionManagement {
    rulesMode = RulesMode.FAIL_ON_PROJECT_RULES
}

plugins {
    id("com.nophasenokill.root-settings-plugin")
}