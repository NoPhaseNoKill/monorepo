
rootProject.name = "test-composite-build"

pluginManagement {
    /*
        Loads repositories and basic settings in a generalised manner.
     */
    includeBuild("composite-build/base-plugins/root-settings-plugin")
    includeBuild("composite-build/base-build-plugins/standalone-kotlin-base-plugin")
}

plugins {
    id("com.nophasenokill.root-settings-plugin")
}

include(":producer-plugin")
project(":producer-plugin").projectDir = file("composite-build/meta-plugins/producer-plugin")


include(":consumer-plugin")
project(":consumer-plugin").projectDir = file("composite-build/standard-plugins/consumer-plugin")