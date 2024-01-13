pluginManagement {
    includeBuild("build-logic/settings")
}

plugins {
    id("kotlin-project-root-settings")
}

rootProject.name = "jvm"

val obfuscatedValue = "obfuscated"

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"

        obfuscation {
            username { obfuscatedValue }
            ipAddresses { addresses -> addresses.map { _ -> "${obfuscatedValue}.${obfuscatedValue}.${obfuscatedValue}.${obfuscatedValue}" } }
            hostname { obfuscatedValue }
        }
    }
}

buildCache {
    local {
        isEnabled = true
        isPush = true
    }
}