rootProject.name = "root-project"

rootDir.resolve("oneLevelDeep").listFiles()?.forEach { topLevelDir ->
    if (topLevelDir.isDirectory) {
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


