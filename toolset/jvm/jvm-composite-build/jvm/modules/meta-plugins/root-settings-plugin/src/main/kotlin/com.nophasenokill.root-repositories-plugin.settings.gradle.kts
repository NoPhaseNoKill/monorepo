pluginManagement {
    repositories {
        flatDir {
            name = "libs"
            dirs = setOf(
                file("$rootProject.projectDir/local-published-plugins")
            )
        }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}
