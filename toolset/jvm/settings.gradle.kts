
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.16.2"
    id("org.gradle.toolchains.foojay-resolver-convention")  version  "0.7.0"
    id("com.gradle.common-custom-user-data-gradle-plugin")  version "1.12.1"
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
    }


    // Pulls this in for free

    //     versionCatalogs {
    //         create("libs") {
    //             from(files("gradle/libs.versions.toml"))
    //         }
    //     }

     // See: https://docs.gradle.org/current/userguide/declaring_repositories.html#ex-enforcing-settings-repositories
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
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

buildCache {
    local {
        isEnabled = true
        isPush = true
    }
}

rootProject.name = "jvm"

include(":modules:platform:generalised-platform")
include(":modules:platform:junit-platform")

include(":modules:libraries:list")
include(":modules:libraries:utilities")

include(":modules:applications:app")
include(":modules:applications:accelerated-test-suite-runner")

include(":modules:standalone-plugins:plugin")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")