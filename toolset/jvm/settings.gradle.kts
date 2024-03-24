rootProject.name = "jvm"

plugins {
    id("com.gradle.enterprise") version "3.16.2"
    id("org.gradle.toolchains.foojay-resolver-convention")  version  "0.7.0"
    id("com.gradle.common-custom-user-data-gradle-plugin")  version "1.12.1"
}

gradleEnterprise {
    buildScan {
        isUploadInBackground = false
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"

        val username = "OBFUSCATED_USERNAME"
        val ipAddress = "OBFUSCATED_IP_ADDRESS"
        val hostname = "OBFUSCATED_HOSTNAME"

        /*
            If you try to abstract these strings to a 'val' outside gradleEnterprise lambda, you get errors
            such as below:

            4 problems were found storing the configuration cache.
            - Gradle runtime: cannot serialize Gradle script object references as these are not supported with the configuration cache.
              See https://docs.gradle.org/8.5/userguide/configuration_cache.html#config_cache:requirements:disallowed_types
                ...
                ...
         */
        obfuscation {
            username { username }
            ipAddresses { addresses -> addresses.map { _ -> ipAddress } }
            hostname { hostname }
        }
    }
}

includeBuild("base/standalone-plugins")
includeBuild("base/modules")
includeBuild("base/platforms")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")