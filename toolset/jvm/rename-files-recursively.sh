#!/bin/bash
# Script to replace any pattern with another in filenames.

if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <old-pattern> <new-pattern>"
  exit 1
fi

old_pattern=$1
new_pattern=$2

# Find all files matching the old pattern and replace it with the new one.
find . -iname "*$old_pattern*" -exec rename "s/$old_pattern/$new_pattern/" '{}' \;

