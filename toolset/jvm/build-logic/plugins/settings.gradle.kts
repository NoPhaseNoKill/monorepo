dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
    includeBuild("../platforms")
}


rootProject.name = "plugins"

include("commons-plugin")
include("base-kotlin-plugin")
include("kotlin-library-plugin")
include("kotlin-application-plugin")
include("capability-conflict-avoidance-plugin")