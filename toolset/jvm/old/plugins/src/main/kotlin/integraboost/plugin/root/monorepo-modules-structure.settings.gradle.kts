package integraboost.plugin.root

pluginManagement {
    repositories.gradlePluginPortal()
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}

// Dynamically include all subprojects under 'modules' directory
file("modules")
    .listFiles()
    ?.filter { it.isDirectory }
    ?.forEach { moduleDir ->
        moduleDir.listFiles()
            ?.filter { it.isDirectory }
            ?.forEach { projectDir ->
                include(":modules:${moduleDir.name}:${projectDir.name}")
            }
    }






