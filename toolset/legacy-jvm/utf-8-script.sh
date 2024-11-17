#!/bin/bash

DIRECTORY=$1  # Directory to search in

# Function to check if a file is UTF-8 encoded
check_utf8() {
  if iconv -f UTF-8 -t UTF-8 "$1" >/dev/null 2>&1; then
    return 0  # UTF-8 encoded
  else
    return 1  # Not UTF-8 encoded
  fi
}

# Find all files that should be UTF-8 and check their encoding
find "$DIRECTORY" -type f \( \
  -name "*.java" -o \
  -name "*.kt" -o \
  -name "*.groovy" -o \
  -name "*.gradle" -o \
  -name "*.gradle.kts" -o \
  -name "*.xml" -o \
  -name "*.properties" -o \
  -name "*.json" -o \
  -name "*.yaml" -o \
  -name "*.yml" -o \
  -name "*.txt" -o \
  -name "*.csv" -o \
  -name "*.md" -o \
  -name "*.html" -o \
  -name "*.css" -o \
  -name "*.js" \) | while read -r file; do
    if ! check_utf8 "$file"; then
      echo "[NOT UTF-8] $file"
    fi
done

