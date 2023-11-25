

// == Define locations for build logic ==
pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    includeBuild("../platforms")
    includeBuild("../build-logic")
}

// == Define locations for components ==
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

// == Define the inner structure of this component ==
rootProject.name = "modules"
include("integraBoostLibrary")
include("integraBoostService")
