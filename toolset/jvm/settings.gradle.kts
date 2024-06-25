rootProject.name = "jvm"

pluginManagement {
    includeBuild("known-to-be-working-examples/plugins/root-settings-plugin")
}

plugins {
    id("com.nophasenokill.root-settings-plugin") version("4.3.1")
}


include("known-to-be-working-examples:plugins:basic-plugin")
include("known-to-be-working-examples:plugins:extended-plugin")
