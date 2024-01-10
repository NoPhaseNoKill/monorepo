
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    includeBuild("build-logic/settings")
    includeBuild("build-logic/plugins")
}

plugins {
    id("kotlin-project-root-settings")
}

rootProject.name = "jvm"
include("modules:applications:app", "modules:libraries:list", "modules:libraries:utilities")

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

buildCache {
    local {
        isEnabled = true
        isPush = true
    }
}
