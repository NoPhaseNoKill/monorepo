rootProject.name = "meta-plugin"

pluginManagement {
    repositories {
        // includeBuild("../standalone-plugin")
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