rootProject.name = "root-project"

pluginManagement {

    includeBuild("oneLevelDeep/build-logic")
    plugins {
        kotlin("jvm") version "1.9.22"
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
    // id("com.nophasenokill.root-settings")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}




rootDir.resolve("oneLevelDeep").listFiles()?.forEach { topLevelDir ->
    if (topLevelDir.isDirectory && topLevelDir.name !== "build-logic" && topLevelDir.name !== "platforms") {
        topLevelDir.listFiles()?.forEach { projectDir ->
            if (projectDir.isDirectory) {
                projectDir.listFiles()?.let { files ->
                    if (files.any { it.name == "build.gradle.kts" }) {
                        val projectPath = "oneLevelDeep:${topLevelDir.name}:${projectDir.name}"

                        logger.quiet("  Including project at: $projectPath")

                        include(projectPath)
                    }
                }
            }
        }
    }
}

logger.quiet("")


