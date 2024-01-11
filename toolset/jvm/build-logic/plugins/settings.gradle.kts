dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
    }
    includeBuild("../platforms")
}

rootProject.name = "plugins"

include("kotlin-base-conventions")
include("kotlin-application-conventions")
include("kotlin-library-conventions")