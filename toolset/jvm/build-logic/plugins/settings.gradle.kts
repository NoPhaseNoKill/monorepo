dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
    }
    includeBuild("../platforms")
}

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "plugins"

include("kotlin-base-conventions")
include("kotlin-application-conventions")
include("kotlin-library-conventions")