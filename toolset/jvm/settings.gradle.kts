pluginManagement {
    repositories {
        gradlePluginPortal()
        exclusiveContent {
            val repos = listOf<ArtifactRepository>(mavenCentral(), mavenLocal())
            forRepositories(*repos.toSet().toTypedArray())
            filter {
                includeGroup("com.nophasenokill")
            }
        }
    }
}

dependencyResolutionManagement {
    includeBuild("modules")
}

plugins {
    id("com.gradle.enterprise") version "3.16.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
    /*
        https://github.com/gradle/common-custom-user-data-gradle-plugin
        Adds some recommendations of tags, links etc from develocity
     */
    id("com.gradle.common-custom-user-data-gradle-plugin") version "1.12.1"
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
            username { "OBFUSCATED_USERNAME" }
            ipAddresses { addresses -> addresses.map { _ -> "OBFUSCATED_IP_ADDRESS" } }
            hostname { "OBFUSCATED_HOSTNAME" }
        }
    }
}

buildCache {
    local {
        isEnabled = true
        isPush = true
    }
}

rootProject.name = "jvm"