pluginManagement {
    repositories {
        gradlePluginPortal()
        includeBuild("../plugins")
    }
}

dependencyResolutionManagement {
    // forces underlying modules to use our own convention plugins
    repositories.gradlePluginPortal()

    // See: https://docs.gradle.org/current/userguide/declaring_repositories.html#ex-enforcing-settings-repositories
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}

plugins {
//    id("kotlin-project-root-settings")
}

rootProject.name = "platform"