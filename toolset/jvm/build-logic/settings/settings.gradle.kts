
dependencyResolutionManagement {
    // forces underlying modules to use our own convention plugins
    repositories.gradlePluginPortal()

    // See: https://docs.gradle.org/current/userguide/declaring_repositories.html#ex-enforcing-settings-repositories
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}

rootProject.name = "settings"

include("jvm-root-settings")