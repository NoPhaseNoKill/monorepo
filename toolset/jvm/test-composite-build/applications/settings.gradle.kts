


pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    includeBuild("../build-logic")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

includeBuild("../platforms")

rootProject.name = "applications"
include("app-with-no-library-dependencies")
include("app-with-single-library-dependencies")
include("app-with-two-or-more-library-dependencies")