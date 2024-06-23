rootProject.name = "platforms"

include("sensible-defaults-platform")
include("test-platform")
include("plugins-platform")

pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.20"
    }
}

// plugins {
//     id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
// }

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

