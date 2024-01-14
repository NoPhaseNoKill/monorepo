
/*
    Be careful with changing or modifying this.
    There is explicitly no pluginManagement set,
    and the dependency resolution management has a specific
    value which should include nothing else.

    If you want to configure what actually gets done for a project
    that uses the underlying kotlin-project-root-settings plugin
    see: kotlin-project-root-repositories.settings.gradle.kts.
 */

dependencyResolutionManagement {
    // forces underlying modules to use our own convention plugins
    repositories.gradlePluginPortal()
}

rootProject.name = "settings"

include("kotlin-project-root-settings")