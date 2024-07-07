#!/bin/bash

# Usage: create-project.sh [lib|app] project_name

# Define directories
BASE_DIR="standalone-projects"
LIB_DIR="${BASE_DIR}/libraries"
APP_DIR="${BASE_DIR}/applications"

# Input parameters
TYPE="$1"
PROJECT_NAME="$2"

if [[ -z "$TYPE" || -z "$PROJECT_NAME" ]]; then
    echo "Usage: $0 [lib|app] project_name"
    exit 1
fi

# Function to convert dash-separated to CamelCase
to_camel_case() {
    echo "$1" | awk -F'-' '{for(i=1;i<=NF;i++){ $i=toupper(substr($i,1,1)) tolower(substr($i,2)) }}1' OFS=''
}

# Determine the target directory and plugin ID based on the type
if [[ "$TYPE" == "lib" ]]; then
    TARGET_DIR="${LIB_DIR}/${PROJECT_NAME}"
    PLUGIN_ID="com.nophasenokill.kotlin-library-plugin"
elif [[ "$TYPE" == "app" ]]; then
    TARGET_DIR="${APP_DIR}/${PROJECT_NAME}"
    PLUGIN_ID="com.nophasenokill.kotlin-application-plugin"
else
    echo "Invalid project type: $TYPE"
    echo "Use 'lib' for library or 'app' for application."
    exit 1
fi

# Create directories
mkdir -p "${TARGET_DIR}/src/main/kotlin/com/nophasenokill"
mkdir -p "${TARGET_DIR}/src/test/kotlin/com/nophasenokill"

# Create build.gradle.kts file
cat << EOF > "${TARGET_DIR}/build.gradle.kts"
plugins {
    id("$PLUGIN_ID")
}
EOF

# Create Kotlin file for applications
if [[ "$TYPE" == "app" ]]; then
    CLASS_NAME=$(to_camel_case "${PROJECT_NAME}")"App"
    cat << EOF > "${TARGET_DIR}/src/main/kotlin/com/nophasenokill/${CLASS_NAME}.kt"
package com.nophasenokill

object $CLASS_NAME {
    fun main() {
        println("Hello world from \${this.javaClass.simpleName}!")
    }
}

fun main() {
    $CLASS_NAME.main()
}

EOF
fi

echo "Project $PROJECT_NAME ($TYPE) created in $TARGET_DIR"
