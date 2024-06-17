rootProject.name = "standard-plugins"

include("kotlin-dsl-plugin")
include("repositories-plugin")
include("group-and-version-plugin")
include("publishing-plugin")
include("java-gradle-applier-plugin")
include("base-kotlin-plugin")

pluginManagement {
    includeBuild("../root-plugins")
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
