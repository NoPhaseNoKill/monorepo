

pluginManagement {
    includeBuild("modules/meta-plugins")
}

plugins {
    /*
        Must be published to $rootDir/local-repo before this can get resolved
     */
    id("com.nophasenokill.root-settings-plugin")
}

rootProject.name = "jvm"



