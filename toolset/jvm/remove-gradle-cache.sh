#!/bin/bash

# This script removes:
# 1. The local gradle cache directories ('build' and '.gradle') recursively
# 2. The gradle user home directory aka ~/.gradle
# 3. Any stored .idea folders in the sub-tree recursively
# 4. The local-repo folders in the sub-tree recursively, which were created as a part of local publishing
# Use this when you want to be absolutely certain there's nothing being cached locally for the project

delete_gradle_cache() {
  # Only deletes build folders which are on the same level as either a settings.gradle.kts or build.gradle.kts file
  # This prevents accidental removal of any actual source folders named 'build'
    find "$1" -type d -name 'build' | while read -r build_dir; do
        parent_dir=$(dirname "$build_dir")
        if [ -f "$parent_dir/build.gradle.kts" ] || [ -f "$parent_dir/settings.gradle.kts" ]; then
            rm -rf "$build_dir"
        fi
    done
    find "$1" -type d -name '.gradle' -exec rm -rf {} +
}

delete_intellij_cache() {
     find "$1" -type d -name '.idea' -exec rm -rf {} +
}

delete_local_repo_cache() {
     find "$1" -type d -name 'local-repo' -exec rm -rf {} +
}

# Check if script is run from the root of a Gradle project
if [[ -f "build.gradle" ]] || [[ -f "build.gradle.kts" ]] || [[ -f "settings.gradle.kts" ]] || [[ -f "settings.gradle" ]]; then
    # Call the function with the current directory
    delete_gradle_cache "$(pwd)"

    echo "Local Gradle cache directories have been removed."

    # Call the function with the current directory
    delete_local_repo_cache "$(pwd)"

    echo "Local published repo directories have been removed."

    rm -rf ~/.gradle
    echo "Gradle user home folder has been removed"

    # Call the function with the current directory
    delete_intellij_cache "$(pwd)"
    echo "IntelliJ cache has been removed"

else
    echo "Error: This script should be run from the root of a Gradle project."
    exit 1
fi
