rootProject.name = "jvm"

pluginManagement {
    repositories.gradlePluginPortal()
}

plugins {
    id("com.gradle.enterprise") version "3.16.2"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {
    repositories.mavenCentral()
    repositories.gradlePluginPortal()
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

includeBuild("composite-build/platforms/generalised-platform") {
    name = "generalised-platform"
}

includeBuild("composite-build/meta-plugins/meta-plugin-one") {
    name = "meta-plugin-one"
}

includeBuild("composite-build/platforms/junit-platform") {
    name = "junit-platform"
}

includeBuild("composite-build/standalone-plugins/standalone-plugin-one") {
    name = "standalone-plugin-one"
}

includeBuild("composite-build/libraries/library-one") {
    name = "library-one"
}

includeBuild("composite-build/applications/application-one") {
    name = "application-one"
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")