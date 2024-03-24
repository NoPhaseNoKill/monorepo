rootProject.name = "jvm"



pluginManagement {

    includeBuild("base/standalone-plugins/plugin")
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
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



include(":platforms:generalised-platform")
project(":platforms:generalised-platform").projectDir = file("base/platforms/generalised-platform")

include(":platforms:junit-platform")
project(":platforms:junit-platform").projectDir = file("base/platforms/junit-platform")

include(":libraries:list")
project(":libraries:list").projectDir = file("base/modules/libraries/list")

include(":libraries:utilities")
project(":libraries:utilities").projectDir = file("base/modules/libraries/utilities")

include(":applications:app")
project(":applications:app").projectDir = file("base/modules/applications/app")

include(":applications:accelerated-test-suite-runner")
project(":applications:accelerated-test-suite-runner").projectDir = file("base/modules/applications/accelerated-test-suite-runner")


enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")