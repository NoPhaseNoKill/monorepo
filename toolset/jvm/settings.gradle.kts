pluginManagement {
    includeBuild("build-logic/settings")
}

plugins {
    id("kotlin-project-root-settings")
}

rootProject.name = "jvm"

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