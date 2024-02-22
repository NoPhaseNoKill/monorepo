plugins {
    id("com.gradle.enterprise")
    id("org.gradle.toolchains.foojay-resolver-convention")
    // id("com.gradle.common-custom-user-data-gradle-plugin") version "1.12.1"
}

gradleEnterprise {
    buildScan {
        isUploadInBackground = false
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"

        capture {
            isBuildLogging = false
            isTestLogging = false
        }

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

buildCache {
    local {
        isEnabled = true
        isPush = true
    }
}