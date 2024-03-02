// pluginManagement {
    // includeBuild("build-logic/settings")
    // includeBuild("build-logic/plugins")

    // include(":base-plugin")
    // project(":base-plugin").projectDir = file("build-logic/plugins/base-plugin")
    //
    // include(":my-kotlin-plugin")
    // project(":my-kotlin-plugin").projectDir = file("build-logic/plugins/my-kotlin-plugin")
    //
    //
    // include(":library-plugin")
    // project(":library-plugin").projectDir = file("build-logic/plugins/library-plugin")
    // include(":application-plugin")
    // project(":application-plugin").projectDir = file("build-logic/plugins/application-plugin")
    //
    // include(":junit-test-plugin")
    // project(":junit-test-plugin").projectDir = file("build-logic/plugins/junit-test-plugin")
// }

dependencyResolutionManagement {
    repositories.gradlePluginPortal()

    // See: https://docs.gradle.org/current/userguide/declaring_repositories.html#ex-enforcing-settings-repositories
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}

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

buildCache {
    local {
        isEnabled = true
        isPush = true
    }
}

// includeBuild("build-logic/platform")
include(":modules:libraries:list")
include(":modules:libraries:utilities")
include(":modules:applications:app")