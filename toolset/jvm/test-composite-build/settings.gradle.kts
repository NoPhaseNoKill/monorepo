
rootProject.name = "test-composite-build"

pluginManagement {
    /*
        Loads repositories and basic settings in a generalised manner.
     */
    includeBuild("composite-build/base-plugins/root-settings-plugin")
}


plugins {
    id("com.nophasenokill.root-settings-plugin")
}

include(":standalone-kotlin-base-plugin")
project(":standalone-kotlin-base-plugin").projectDir = file("composite-build/base-build-plugins/standalone-kotlin-base-plugin")

include(":producer-plugin")
project(":producer-plugin").projectDir = file("composite-build/meta-plugins/producer-plugin")


include(":consumer-plugin")
project(":consumer-plugin").projectDir = file("composite-build/standard-plugins/consumer-plugin")