
pluginManagement {
    repositories.gradlePluginPortal()
}


dependencyResolutionManagement {
    repositories.mavenCentral()
}

fun includeBuildsAndProjects(dir: File) {
    dir.walk().forEach { file ->
        if (file.isFile && file.name == "build.gradle.kts") {
            // Include any projects
            val relativePath = file.parentFile.relativeTo(rootDir).path.replace(File.separator, ":")
            include(relativePath)
            project(":$relativePath").projectDir = file.parentFile
        } else if (file.isFile && file.name == "settings.gradle.kts") {
            // Include any builds
            includeBuild(file.parentFile)
        }
    }
}



includeBuildsAndProjects(rootDir)
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
