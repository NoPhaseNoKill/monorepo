rootProject.name="build-conventions"
include("java-conventions")
include("common-conventions")

/*
    includeBuild("java-conventions")
    includeBuild("common-conventions")

    or

    include("java-conventions")
    include("common-conventions")
 */

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}
