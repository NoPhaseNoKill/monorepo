dependencyResolutionManagement {
    // forces underlying modules to use our own convention plugins
    repositories.gradlePluginPortal()
}

rootProject.name = "settings"

include("kotlin-project-root-settings")