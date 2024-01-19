pluginManagement {
    includeBuild("build-logic/settings")
}

plugins {
    id("kotlin-project-root-repositories")
    id("com.gradle.enterprise") version "3.15.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
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