

pluginManagement {

    repositories.gradlePluginPortal()
}

dependencyResolutionManagement {

    includeBuild("build-logic/plugins")
    repositories.gradlePluginPortal()  // ensures that we have access to our own convention plugins

    // See: https://docs.gradle.org/current/userguide/declaring_repositories.html#ex-enforcing-settings-repositories
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
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

rootProject.name = "jvm"


gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"

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

// Dynamically includes top level directories within each of the modules' sub-folders
val directories = setOf("applications", "libraries")
directories.forEach { dir ->
    rootDir
            .resolve("modules/${dir}")
            .listFiles { file -> file.isDirectory && !file.isHidden }
            ?.forEach {
                include("modules:$dir:${it.name}")
            }
}


enableFeaturePreview("STABLE_CONFIGURATION_CACHE")