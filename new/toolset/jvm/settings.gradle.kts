rootProject.name = "integraboost"

pluginManagement {
    repositories.gradlePluginPortal()
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}

// Dynamically include all subprojects under 'modules' directory
file("modules").listFiles()?.forEach { moduleDir ->
    moduleDir.listFiles()?.forEach { projectDir ->
        if (projectDir.isDirectory) {
            include(":modules:${moduleDir.name}:${projectDir.name}")
        }
    }
}