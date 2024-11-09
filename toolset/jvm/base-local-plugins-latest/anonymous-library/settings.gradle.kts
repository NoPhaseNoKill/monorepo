rootProject.name = "anonymous-library"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

gradle.lifecycle.beforeProject {
    this.repositories.mavenCentral()
}
