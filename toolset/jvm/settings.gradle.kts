rootProject.name = "jvm"

pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

val standaloneProjectDirs = setOf("applications", "libraries")
val exclusionFolders = setOf("build", "gradle")

standaloneProjectDirs.forEach { standaloneProjectDir ->
    file("standalone-projects/${standaloneProjectDir}")
        .listFiles()
        ?.filter { file -> file.isDirectory && !file.isHidden && !exclusionFolders.contains(file.name)}
        ?.map { dir ->
            include(dir.name)
            project(":${dir.name}").projectDir = dir
        }
}
