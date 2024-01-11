dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
    includeBuild("../platforms")
}


rootProject.name = "plugins"

include("commons-plugin")
include("kotlin-library-plugin")
include("kotlin-application-plugin")