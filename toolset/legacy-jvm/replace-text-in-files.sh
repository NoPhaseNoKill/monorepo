#!/bin/bash
# Script to replace text in files

if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <old-pattern> <new-pattern>"
  exit 1
fi

old_pattern=$1
new_pattern=$2

# Find all files and replace the old pattern with the new one
find . -type f -exec sed -i "s|$old_pattern|$new_pattern|g" {} +

echo "Text replacement completed."

