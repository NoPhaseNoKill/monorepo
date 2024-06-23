#!/bin/bash

# Define the base directory
BASE_DIR="oneLevelDeep"

# Create the base directory and subdirectories for binary plugins, libraries, and applications
mkdir -p "$BASE_DIR/binary-plugins/plugin1/src/main/kotlin/nophasenokill"
mkdir -p "$BASE_DIR/binary-plugins/plugin1/src/test/kotlin/nophasenokill"

mkdir -p "$BASE_DIR/binary-plugins/plugin2/src/main/kotlin/nophasenokill"
mkdir -p "$BASE_DIR/binary-plugins/plugin2/src/test/kotlin/nophasenokill"

mkdir -p "$BASE_DIR/libraries/lib1/src/main/kotlin/nophasenokill"
mkdir -p "$BASE_DIR/libraries/lib2/src/test/kotlin/nophasenokill"

mkdir -p "$BASE_DIR/applications/app1/src/main/kotlin/nophasenokill"
mkdir -p "$BASE_DIR/applications/app2/src/test/kotlin/nophasenokill"

# Create build.gradle.kts files
touch "settings.gradle.kts"
touch "build.gradle.kts"

touch "$BASE_DIR/binary-plugins/plugin1/build.gradle.kts"
touch "$BASE_DIR/binary-plugins/plugin2/build.gradle.kts"
touch "$BASE_DIR/libraries/lib1/build.gradle.kts"
touch "$BASE_DIR/libraries/lib2/build.gradle.kts"
touch "$BASE_DIR/applications/app1/build.gradle.kts"
touch "$BASE_DIR/applications/app2/build.gradle.kts"

# Create and write to settings.gradle.kts in the root directory
cat > "settings.gradle.kts" <<EOL
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
EOL

# Create and write to build.gradle.kts in the root directory
cat > "build.gradle.kts" <<EOL
plugins {
    `java` // Apply Java plugin globally if needed
}

allprojects {
    repositories {
        mavenCentral()
    }

    // Enable build cache
    // gradle.startParameter.isBuildCacheEnabled = true
    // gradle.startParameter.isConfigurationCacheEnabled = true
}

subprojects {
    apply(plugin = "java")


}
EOL
