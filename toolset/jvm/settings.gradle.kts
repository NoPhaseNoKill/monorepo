rootProject.name = "jvm"

plugins {
    id("com.gradle.enterprise") version "3.16.2"
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

/*
    https://scans.gradle.com/s/wtpk3zm6j4ejq/performance/execution

    Serial execution factor 7.7x
 */

includeBuild("composite-build/meta-plugins") {
    name = "meta-plugins"
}
includeBuild("composite-build/libraries") {
    name = "libraries"
}
includeBuild("composite-build/applications") {
    name = "applications"
}
includeBuild("composite-build/platforms") {
    name = "platforms"
}
includeBuild("composite-build/standalone-plugins") {
    name = "standalone-plugins"
}


enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")