

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
        isUploadInBackground = false
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

includeBuild("build-logic/plugins-tested")

val directories = setOf("applications", "libraries")

directories.forEach { moduleCategory ->
    rootDir
        .resolve("modules/${moduleCategory}")
        .listFiles { file -> file.isDirectory && !file.isHidden && !file.name.startsWith("gradle")}
        ?.forEach { projectDirectory: File ->
            /*
                This specifically means each subproject is accessible from :projectName.

                Another benefit is that it only includes the specific leaf node directory
                and NOT any directories in-between the root (where this file is located)
                and the included directory. A HUGE downside of the old approach as default
                behaviour, is that the OBSERVED speed of dependency resolution is significantly
                slower. Currently, I do not quite understand why, but my suspicion is that
                instead of checking for dependency resolution against a single folder (what we want),
                it would previously check against 3, thereby slowing it down by 200% in comparison.

                Taken from the Settings.java docs:

                "As an example, the path a:b adds a project with path :a:b, name b and project
                directory $rootDir/a/b. It also adds the a project with path :a, name a and project
                directory $rootDir/a, if it does not exist already."

                Meaning:

                    include("modules/applications/app")

                    - Adds a project with path ':modules', name 'modules', and project directory '$rootDir/modules'
                    - Adds a project 'modules/applications' with path ':modules:applications', name 'applications', and project directory '$rootDir/modules/applications'
                    - Adds a project 'modules/applications/app' with path ':modules:applications:app', name 'app', and project directory '$rootDir/modules/applications/app'

                The first two of which are completely unnecessary in our use case.

                Please note: IntelliJ currently chooses to display the subsequent folder incorrectly.
                    See: https://youtrack.jetbrains.com/issue/IDEA-82965/Clean-module-names for fix and details
             */
            include(projectDirectory.name)
            project(":${projectDirectory.name}").projectDir = file(projectDirectory.path)
        }
}


enableFeaturePreview("STABLE_CONFIGURATION_CACHE")