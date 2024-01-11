dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    includeBuild("../platforms")
}

rootProject.name = "settings"

include("kotlin-project-root-settings")