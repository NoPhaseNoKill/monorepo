package integraboost.plugin.root

plugins {
    id("com.gradle.enterprise")
}

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
