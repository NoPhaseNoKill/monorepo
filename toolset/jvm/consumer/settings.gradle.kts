rootProject.name = "consumer"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
    }
}

includeBuild("../producer") {
    dependencySubstitution {
        substitute(module("producer:producer")).using(project(":"))
    }
}
